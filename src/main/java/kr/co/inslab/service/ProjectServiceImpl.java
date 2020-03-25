package kr.co.inslab.service;


import kr.co.inslab.exception.APIException;
import kr.co.inslab.exception.KeyCloakAdminException;
import kr.co.inslab.keycloak.AbstractKeyCloak;
import kr.co.inslab.bootstrap.KeyCloakAdminConfig;
import kr.co.inslab.bootstrap.StaticConfig;
import kr.co.inslab.model.Group;
import kr.co.inslab.model.Member;
import kr.co.inslab.model.Project;
import kr.co.inslab.util.HTMLTemplate;
import kr.co.inslab.util.SimpleToken;
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
import javax.ws.rs.core.Response;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class ProjectServiceImpl extends AbstractKeyCloak implements ProjectService {

    private final Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);

    private final RedisTemplate<String,Object> redisTemplate;

    private final MailSendingService mailSendingService;

    private final String NO_REPLY_GANTRY_AI = "noreply@gantry.ai";

    private final HTMLTemplate htmlTemplate;


    public ProjectServiceImpl(Keycloak keycloakAdmin, RedisTemplate<String, Object> redisTemplate, MailSendingService mailSendingService, HTMLTemplate htmlTemplate) {
        super(keycloakAdmin);
        this.redisTemplate = redisTemplate;
        this.mailSendingService = mailSendingService;
        this.htmlTemplate = htmlTemplate;
    }


    @Override
    public Boolean existsUserInProject(String userId, String projectId){
        Boolean existsUserInProject = false;
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
    public void checkUserById(String userId) throws APIException{
        try{
            this.getUserResourceById(userId).toRepresentation();
        }catch (Exception e){
            if(e instanceof javax.ws.rs.WebApplicationException) {
                String message = ((WebApplicationException)e).getResponse().getStatusInfo().getReasonPhrase();
                int code = ((WebApplicationException)e).getResponse().getStatusInfo().getStatusCode();
                throw new APIException("[user_id : "+userId+"] "+message, HttpStatus.resolve(code));
            }
            throw e;
        }
    }


    @Override
    public Project getProjectById(String projectId) throws APIException{
        GroupRepresentation groupRepresentation = null;
        try{
            groupRepresentation = this.getGroupById(projectId);
        }catch (Exception e){
            if(e instanceof javax.ws.rs.WebApplicationException) {
                String message = ((WebApplicationException)e).getResponse().getStatusInfo().getReasonPhrase();
                int code = ((WebApplicationException)e).getResponse().getStatusInfo().getStatusCode();
                throw new APIException("[project_id : "+projectId+"] "+message, HttpStatus.resolve(code));
            }
            throw e;
        }

        Project project = makeProjectInfo(groupRepresentation);
        return project;
    }

    @Override
    public void updateProjectInfo(String projectId, Map<String,String> attrs) {

        GroupRepresentation groupRepresentation = this.getGroupById(projectId);

        if(attrs != null){
            for(String key:attrs.keySet()){
                groupRepresentation.singleAttribute(key,attrs.get(key));
            }
        }

        this.updateGroup(groupRepresentation.getId(),groupRepresentation);

    }

    @Override
    public Boolean isOwnerOfProject(String userId,String projectId) {
        Boolean isOwner = false;
        GroupRepresentation groupRepresentation = this.getGroupById(projectId);
        Project project = this.makeProjectInfo(groupRepresentation);
        if (project.getOwner().equals(userId)){
            isOwner = true;
        }
        return isOwner;
    }

    @Override
    public void deleteProjectById(String projectId) {
        this.removeGroupById(projectId);
    }

    @Override
    public List<Group> getGroupsByProjectId(String projectId) throws APIException{
        List<Group> groups = null;
        Project project = this.getProjectById(projectId);
        groups = project.getGroups();
        return groups;
    }

    @Override
    public void inviteUserToGroup(String email,String projectId,String groupId) throws KeyCloakAdminException, APIException {

        List<UserRepresentation> userRepresentations = this.getUserByEmail(email);

        //New User
        if(userRepresentations == null || userRepresentations.size() == 0){
            //TODO: 생성 error 처리 추가해야 함
            GroupRepresentation topGroup = this.getGroupById(projectId);
            GroupRepresentation subGroup = this.getGroupById(groupId);
            UserRepresentation userRepresentation = this.createUser(email);
            UserResource userResource = this.getUserResourceById(userRepresentation.getId());
            this.joinGroup(userRepresentation.getId(),topGroup.getId());
            this.joinGroup(userRepresentation.getId(),subGroup.getId());
            userResource.sendVerifyEmail();
        //Exists User
        }else if(userRepresentations.size()==1){

            GroupRepresentation topGroup = this.getGroupById(projectId);
            String projectName = topGroup.getAttributes().get(StaticConfig.DISPLAY_NAME).get(0);
            String token = SimpleToken.generateNewToken();

            Map<String,String> joinInfo = this.makeJoinInfo(projectId,groupId,userRepresentations.get(0).getId(),email);

            //Save to redis
            ValueOperations<String,Object> valueOperations = redisTemplate.opsForValue();
            valueOperations.set(token,joinInfo);
            redisTemplate.expire(token,24,TimeUnit.HOURS);

            //Send email
            String inviteHtml = htmlTemplate.makeInviteHtml(StaticConfig.INVITE,token);
            mailSendingService.sendHtmlEmail(NO_REPLY_GANTRY_AI,email,StaticConfig.GANTRY+"["+projectName+"]초대 메일",inviteHtml);
            this.addPendingUser(topGroup,email);

        //Multiple Users
        }else{
            for(UserRepresentation userRepresentation: userRepresentations){
                logger.debug(userRepresentation.getEmail());
            }
            logger.error("Multiple Users Exception");
            throw new APIException("Multiple Users Exception",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void checkProjectByProjectId(String projectId) {
        this.getGroupById(projectId);
    }

    @Override
    public List<Member> getSubGroupMember(String projectId,String groupId) {
        List<Member> members = new ArrayList<Member>();

        GroupRepresentation groupRepresentation = this.getGroupById(groupId);
        List<UserRepresentation> userRepresentations = this.getMembersByGroupId(groupRepresentation.getId());

        for(UserRepresentation user : userRepresentations){
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
    public Boolean joinNewProjectAndGroupForExistsUser(String token) {

        ValueOperations<String,Object> valueOperations = redisTemplate.opsForValue();

        HashMap<String,String> joinInfo = (HashMap<String, String>) valueOperations.get(token);

        if(joinInfo != null){
            String groupId = joinInfo.get(StaticConfig.GROUP_ID);
            String projectId = joinInfo.get(StaticConfig.PROJECT_ID);
            String userId = joinInfo.get(StaticConfig.USER_ID);
            String email = joinInfo.get(StaticConfig.EMAIL);
            GroupRepresentation groupRepresentation = this.getGroupById(projectId);

            this.joinGroup(userId,groupId);
            this.joinGroup(userId,projectId);
            this.removePendingUser(groupRepresentation,email);

           return true;
        }

        return false;
    }

    private Map<String,String> makeJoinInfo(String projectId,String groupId,String userId,String email){
        Map<String,String> joinInfo = new HashMap<>();
        joinInfo.put(StaticConfig.PROJECT_ID,projectId);
        joinInfo.put(StaticConfig.GROUP_ID,groupId);
        joinInfo.put(StaticConfig.USER_ID,groupId);
        joinInfo.put(StaticConfig.EMAIL,email);
        return joinInfo;
    }

    private final void addPendingUser(GroupRepresentation groupRepresentation,String email){
        Map<String, List<String>> projectAttrs = null;
        List<String> pendingUsers = null;

        projectAttrs = groupRepresentation.getAttributes();
        pendingUsers = projectAttrs.get(StaticConfig.PENDING);

        if(pendingUsers == null){
            pendingUsers = new ArrayList<>();
        }

        if(!pendingUsers.contains(email)){
            pendingUsers.add(email);
            projectAttrs.put(StaticConfig.PENDING,pendingUsers);
            groupRepresentation.setAttributes(projectAttrs);
            this.updateGroup(groupRepresentation.getId(),groupRepresentation);
        }

    }

    public void removePendingUser(GroupRepresentation groupRepresentation,String email) {
        Map<String, List<String>> projectAttrs = null;
        List<String> pendingUsers = null;

        projectAttrs = groupRepresentation.getAttributes();
        pendingUsers = projectAttrs.get(StaticConfig.PENDING);

        if(pendingUsers !=null && pendingUsers.contains(email)){
            pendingUsers.remove(email);
            projectAttrs.put(StaticConfig.PENDING,pendingUsers);
            groupRepresentation.setAttributes(projectAttrs);
            this.updateGroup(groupRepresentation.getId(),groupRepresentation);
        }
    }


    private UserRepresentation createUser(String email) throws KeyCloakAdminException {
        String [] splitEmail = email.split("@");
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setEmailVerified(false);
        userRepresentation.setEnabled(true);
        userRepresentation.setEmail(email);
        userRepresentation.setUsername(splitEmail[1]);

        List<String> actions = new ArrayList<String>();
        actions.add(StaticConfig.UPDATE_PROFILE);
        actions.add(StaticConfig.UPDATE_PASSWORD);
        actions.add(StaticConfig.VERIFY_EMAIL);
        userRepresentation.setRequiredActions(actions);

        Response response = this.getRealm().users().create(userRepresentation);
        String createdId = getCreatedId(response,email);
        userRepresentation.setId(createdId);

        return userRepresentation;
    }


}
