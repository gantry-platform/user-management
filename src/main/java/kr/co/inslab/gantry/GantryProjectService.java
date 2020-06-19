package kr.co.inslab.gantry;

import kr.co.inslab.utils.CommonConstants;
import kr.co.inslab.keycloak.KeyCloakAdminException;
import kr.co.inslab.keycloak.AbstractKeyCloak;
import kr.co.inslab.model.*;
import kr.co.inslab.utils.HTMLTemplate;
import kr.co.inslab.utils.MailSending;
import kr.co.inslab.utils.SimpleToken;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.ws.rs.WebApplicationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class GantryProjectService extends AbstractKeyCloak implements GantryProject {

    private final Logger logger = LoggerFactory.getLogger(GantryProjectService.class);

    private final RedisTemplate<String,Object> redisTemplate;

    private final MailSending mailSending;

    private final HTMLTemplate htmlTemplate;

    public GantryProjectService(Keycloak keycloakAdmin, RedisTemplate<String, Object> redisTemplate, MailSending mailSending, HTMLTemplate htmlTemplate) {
        super(keycloakAdmin);
        this.redisTemplate = redisTemplate;
        this.mailSending = mailSending;
        this.htmlTemplate = htmlTemplate;
    }


    @Override
    public Project createProject(String userId, String displayName, String description) throws ProjectException {
        String projectId = null;
        GroupRepresentation projectGroupRep = null;

        String projectName = userId+"_"+displayName;
        String adminGroupName = SubGroup.ADMIN.toString();
        String opsGroupName = SubGroup.OPS.toString();
        String devGroupName = SubGroup.DEV.toString();

        Map<String,String> groupAttr = new HashMap<>();
        groupAttr.put(CommonConstants.DISPLAY_NAME,displayName);
        groupAttr.put(CommonConstants.OWNER,userId);
        groupAttr.put(CommonConstants.STATUS, kr.co.inslab.model.Project.StatusEnum.ACTIVE.toString());

        if(!description.isEmpty()){
            groupAttr.put(CommonConstants.DESCRIPTION,description);
        }

        try{
            projectGroupRep = this.createGroup(projectName,groupAttr);
            projectId = projectGroupRep.getId();
            GroupRepresentation adminGroupRep = this.addSubGroup(projectGroupRep, adminGroupName);
            GroupRepresentation opsGroupRep = this.addSubGroup(projectGroupRep, opsGroupName);
            GroupRepresentation devGroupRep = this.addSubGroup(projectGroupRep, devGroupName);
            this.addRoleToGroup(adminGroupRep, Role.ADMIN);
            this.addRoleToGroup(opsGroupRep, Role.OPS);
            this.addRoleToGroup(devGroupRep, Role.DEV);
            this.joinGroup(userId,projectGroupRep.getId());
            this.joinGroup(userId,adminGroupRep.getId());
        } catch (Exception e){
            if(projectGroupRep != null){
                this.removeGroupById(projectGroupRep.getId());
                throw new ProjectException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            if (e instanceof KeyCloakAdminException){
                throw new ProjectException((e.getMessage()),((KeyCloakAdminException) e).getHttpStatus());
            }
        }

        kr.co.inslab.model.Project project = new kr.co.inslab.model.Project();
        project.setDisplayName(displayName);
        project.setName(projectName);
        project.setId(projectId);

        return project;
    }

    @Override
    public Boolean existsUserInProject(String userId, String projectId){
        boolean existsUserInProject = false;
        List<GroupRepresentation> groupRepresentations = this.getGroupsByUserId(userId);
        for(GroupRepresentation groupRepresentation : groupRepresentations){
            if(groupRepresentation.getId().equals(projectId)){
                existsUserInProject = true;
                break;
            }
        }
        return existsUserInProject;
    }

    @Override
    public kr.co.inslab.model.Project getProjectById(String projectId) throws ProjectException {
        GroupRepresentation groupRepresentation;
        try{
            groupRepresentation = this.getGroupById(projectId);
        }catch (Exception e){
            if(e instanceof javax.ws.rs.WebApplicationException) {
                String message = ((WebApplicationException)e).getResponse().getStatusInfo().getReasonPhrase();
                int code = ((WebApplicationException)e).getResponse().getStatusInfo().getStatusCode();
                throw new ProjectException("[project_id : "+projectId+"] "+message, HttpStatus.resolve(code));
            }
            throw e;
        }
        return makeProjectInfo(groupRepresentation);
    }

    @Override
    public void updateProjectInfo(String projectId, Map<String,String> attrs) throws ProjectException {
        GroupRepresentation groupRepresentation;
        try{
            groupRepresentation = this.getGroupById(projectId);
        }catch (Exception e){
            if(e instanceof javax.ws.rs.WebApplicationException) {
                String message = ((WebApplicationException)e).getResponse().getStatusInfo().getReasonPhrase();
                int code = ((WebApplicationException)e).getResponse().getStatusInfo().getStatusCode();
                throw new ProjectException("[project_id : "+projectId+"] "+message, HttpStatus.resolve(code));
            }
            throw e;
        }
        if(null != attrs){
            for(String key:attrs.keySet()){
                groupRepresentation.singleAttribute(key,attrs.get(key));
            }
        }
        this.updateGroup(groupRepresentation.getId(),groupRepresentation);
    }

    @Override
    public Boolean isOwnerOfProject(String userId,String projectId) {
        boolean isOwner = false;
        GroupRepresentation groupRepresentation = this.getGroupById(projectId);
        kr.co.inslab.model.Project project = this.makeProjectMetaInfo(groupRepresentation);
        if (project.getOwner().equals(userId)){
            isOwner = true;
        }
        return isOwner;
    }

    @Override
    public Boolean isAdminOfProject(String userId, String projectId) {
        boolean isAdmin = false;

        GroupRepresentation groupRepresentation = this.getGroupById(projectId);
        List<GroupRepresentation> subGroups = groupRepresentation.getSubGroups();

        for(GroupRepresentation subGroup : subGroups){
            if(subGroup.getName().equals(SubGroup.ADMIN.toString())){
                List<UserRepresentation> userRepresentations = this.getMembersByGroupId(subGroup.getId());
                for(UserRepresentation userRepresentation : userRepresentations){
                    if(userRepresentation.getId().equals(userId)){
                        isAdmin = true;
                        break;
                    }
                }
                break;
            }
        }
        return isAdmin;
    }

    @Override
    public void deleteProjectById(String projectId) {
        this.removeGroupById(projectId);
    }

    @Override
    public void deleteMemberInProject(String projectId, String memberId) {
        List<GroupRepresentation> memberGroups = this.getGroupsByUserId(memberId);

        for(GroupRepresentation memberGroup : memberGroups){
            this.leaveGroup(memberId, memberGroup.getId());
        }
    }

    @Override
    public void deleteMemberInPending(String projectId,String email) throws ProjectException {
        List<UserRepresentation> userRepresentations = this.getUserByEmail(email);
        //Not Found User
        if(userRepresentations.size()==0){
            throw new ProjectException("["+email+"]"+"Not Found User", HttpStatus.NOT_FOUND);
            //Exists User
        }else if(userRepresentations.size()==1){
            GroupRepresentation groupRepresentation = this.getGroupById(projectId);
            this.removePendingUser(groupRepresentation,email);
            try{
                redisTemplate.delete(email);
            }catch (Exception ex){
                ex.printStackTrace();
                logger.warn("Redis Key 삭제 중 Error");
            }
            if(!(userRepresentations.get(0).isEmailVerified())){
                this.removeUser(userRepresentations.get(0).getId());
            }
            //Multiple Users
        }else{
            for(UserRepresentation userRepresentation: userRepresentations){
                logger.debug(userRepresentation.getEmail());
            }
            logger.error("Multiple Users Exception");
            throw new ProjectException("Multiple Users Exception",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<Group> getGroupsByProjectId(String projectId) throws ProjectException {
        kr.co.inslab.model.Project project = this.getProjectById(projectId);
        return project.getGroups();
    }

    @Override
    public void inviteUserToGroup(String email,String projectId,String groupId) throws ProjectException {
        List<UserRepresentation> userRepresentations = this.getUserByEmail(email);
        //New User
        if((userRepresentations == null) || (userRepresentations.size() == 0)){
            //TODO: 생성 error 처리 추가해야 함
            String userId = null;
            GroupRepresentation topGroup = this.getGroupById(projectId);
            GroupRepresentation subGroup = this.getGroupById(groupId);
            try{
                userId = super.createUser(email);
            }catch (KeyCloakAdminException e){
                throw new ProjectException(e.getMessage(),e.getHttpStatus());
            }
            UserResource userResource = this.getUserResourceById(userId);
            this.joinGroup(userId,topGroup.getId());
            this.joinGroup(userId,subGroup.getId());
            userResource.sendVerifyEmail();
            //Exists User
        }else if(userRepresentations.size()==1){
            //Make Join Info
            GroupRepresentation topGroup = this.getGroupById(projectId);
            String projectName = topGroup.getAttributes().get(CommonConstants.DISPLAY_NAME).get(0);
            String token = SimpleToken.generateNewToken();
            Map<String,String> joinInfo = this.makeJoinInfo(projectId,groupId,userRepresentations.get(0).getId(),token);

            //Save to redis
            ValueOperations<String,Object> valueOperations = redisTemplate.opsForValue();
            valueOperations.set(email,joinInfo);
            redisTemplate.expire(token,24, TimeUnit.HOURS);

            //Send email
            String inviteHtml = htmlTemplate.makeInviteHtml(CommonConstants.INVITE,token,email);
            mailSending.sendHtmlEmail(CommonConstants.NO_REPLY_GANTRY_AI,email, CommonConstants.GANTRY+ " Project 초대 메일",inviteHtml);
            this.addPendingUser(topGroup,email);
            //Multiple Users
        }else{
            for(UserRepresentation userRepresentation: userRepresentations){
                logger.debug(userRepresentation.getEmail());
            }
            logger.error("Multiple Users Exception");
            throw new ProjectException("Multiple Users Exception",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void checkProjectByProjectId(String projectId) {
        this.getGroupById(projectId);
    }

    @Override
    public List<Member> getSubGroupMember(String projectId,String groupId) {
        List<Member> members = null;

        GroupRepresentation groupRepresentation = this.getGroupById(groupId);
        List<UserRepresentation> userRepresentations = this.getMembersByGroupId(groupRepresentation.getId());

        for(UserRepresentation user : userRepresentations){
            if(members == null){
                members = new ArrayList<>();
            }
            Member member = new Member();
            member.setUserName(user.getUsername());
            member.setEmailVerified(user.isEmailVerified());
            member.setUserId(user.getId());
            member.setEmail(user.getEmail());
            members.add(member);
        }
        return members;
    }

    @Override
    public void moveGroupOfMember(String projectId, String groupId, String memberId) {
        List<GroupRepresentation> groupRepresentations = this.getGroupsByUserId(memberId);
        for(GroupRepresentation groupRepresentation : groupRepresentations){
            if(!groupRepresentation.getId().equals(projectId)){
                this.leaveGroup(memberId,groupRepresentation.getId());
            }
        }
        this.joinGroup(memberId,groupId);
    }


    @Override
    public Boolean joinNewProjectAndGroupForExistsUser(String mailToken, String email) throws ProjectException{
        ValueOperations<String,Object> valueOperations = redisTemplate.opsForValue();
        @SuppressWarnings("unchecked")
        HashMap<String,String> joinInfo = (HashMap<String, String>) valueOperations.get(email);

        if(joinInfo != null){
            String groupId = joinInfo.get(CommonConstants.GROUP_ID);
            String projectId = joinInfo.get(CommonConstants.PROJECT_ID);
            String userId = joinInfo.get(CommonConstants.USER_ID);
            String savedToken = joinInfo.get(CommonConstants.TOKEN);

            if(!(savedToken.equals(mailToken))){
                throw new ProjectException("Invalid Request",HttpStatus.BAD_REQUEST);
            }

            GroupRepresentation groupRepresentation = this.getGroupById(projectId);
            this.joinGroup(userId,projectId);
            this.joinGroup(userId,groupId);

            this.removePendingUser(groupRepresentation,email);

            return true;
        }
        return false;
    }

    private Map<String,String> makeJoinInfo(String projectId,String groupId,String userId,String token){
        Map<String,String> joinInfo = new HashMap<>();
        joinInfo.put(CommonConstants.PROJECT_ID,projectId);
        joinInfo.put(CommonConstants.GROUP_ID,groupId);
        joinInfo.put(CommonConstants.USER_ID,userId);
        joinInfo.put(CommonConstants.TOKEN,token);
        return joinInfo;
    }

    private void addPendingUser(GroupRepresentation groupRepresentation, String email){
        Map<String, List<String>> projectAttrs = groupRepresentation.getAttributes();
        List<String> pendingUsers = projectAttrs.get(CommonConstants.PENDING);

        if(pendingUsers == null){
            pendingUsers = new ArrayList<>();
        }

        if(!pendingUsers.contains(email)){
            pendingUsers.add(email);
            projectAttrs.put(CommonConstants.PENDING,pendingUsers);
            groupRepresentation.setAttributes(projectAttrs);
            this.updateGroup(groupRepresentation.getId(),groupRepresentation);
        }

    }

    public void removePendingUser(GroupRepresentation groupRepresentation,String email) {
        Map<String, List<String>> projectAttrs = groupRepresentation.getAttributes();
        List<String> pendingUsers = projectAttrs.get(CommonConstants.PENDING);

        if(pendingUsers !=null && pendingUsers.contains(email)){
            pendingUsers.remove(email);
            projectAttrs.put(CommonConstants.PENDING,pendingUsers);
            groupRepresentation.setAttributes(projectAttrs);
            this.updateGroup(groupRepresentation.getId(),groupRepresentation);
        }
    }
}
