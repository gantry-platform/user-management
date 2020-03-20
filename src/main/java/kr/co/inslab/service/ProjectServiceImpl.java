package kr.co.inslab.service;


import kr.co.inslab.exception.APIException;
import kr.co.inslab.exception.KeyCloakAdminException;
import kr.co.inslab.keycloak.AbstractKeyCloak;
import kr.co.inslab.keycloak.KeyCloakAdmin;
import kr.co.inslab.keycloak.KeyCloakStaticConfig;
import kr.co.inslab.model.Group;
import kr.co.inslab.model.Member;
import kr.co.inslab.model.Project;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ProjectServiceImpl extends AbstractKeyCloak implements ProjectService {

    private final Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);

    public ProjectServiceImpl(KeyCloakAdmin keyCloakAdmin) {
        super(keyCloakAdmin);
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
    public void inviteUserToGroup(String email,String projectId,String groupId) throws KeyCloakAdminException{

        List<UserRepresentation> userRepresentations = this.getUserByEmail(email);

        //New User
        if(userRepresentations == null || userRepresentations.size() == 0){

            GroupRepresentation topGroup = this.getGroupById(projectId);
            GroupRepresentation subGroup = this.getGroupById(groupId);
            UserRepresentation userRepresentation = this.createUser(email);
            UserResource userResource = this.getUserResourceById(userRepresentation.getId());
            this.joinGroup(userRepresentation.getId(),topGroup.getId());
            this.joinGroup(userRepresentation.getId(),subGroup.getId());
            userResource.sendVerifyEmail();
        //Exists USer
        }else{

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

    private UserRepresentation createUser(String email) throws KeyCloakAdminException {
        String [] splitEmail = email.split("@");
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setEmailVerified(false);
        userRepresentation.setEnabled(true);
        userRepresentation.setEmail(email);
        userRepresentation.setUsername(splitEmail[1]);

        List<String> actions = new ArrayList<String>();
        actions.add(KeyCloakStaticConfig.UPDATE_PROFILE);
        actions.add(KeyCloakStaticConfig.UPDATE_PASSWORD);
        actions.add(KeyCloakStaticConfig.VERIFY_EMAIL);
        userRepresentation.setRequiredActions(actions);

        Response response = this.getRealm().users().create(userRepresentation);
        String createdId = getCreatedId(response,email);
        userRepresentation.setId(createdId);

        return userRepresentation;
    }


}
