package kr.co.inslab.service;


import kr.co.inslab.exception.APIException;
import kr.co.inslab.exception.KeyCloakAdminException;
import kr.co.inslab.keycloak.AbstractKeyCloak;
import kr.co.inslab.keycloak.KeyCloakAdmin;
import kr.co.inslab.model.Group;
import kr.co.inslab.model.Project;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.ws.rs.WebApplicationException;
import java.util.List;
import java.util.Map;

@Service
public class ProjectServiceImpl extends AbstractKeyCloak implements ProjectService {

    private final Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);

    public ProjectServiceImpl(KeyCloakAdmin keyCloakAdmin) {
        super(keyCloakAdmin);
    }


    @Override
    public Boolean existsUserInProject(String userId, String projectName){
        Boolean existsUserInProject = false;
        List<GroupRepresentation> groupRepresentations = this.getGroupsByUserId(userId);

        for(GroupRepresentation groupRepresentation : groupRepresentations){
            if(groupRepresentation.getName().equals(projectName)){
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
    public Project getProjectByProjectName(String projectName) throws APIException{
        GroupRepresentation groupRepresentation = null;
        String groupPath = this.projectNameToGroupPath(projectName);;
        try{
            groupRepresentation = this.getGroupByGroupPath(groupPath);
        }catch (Exception e){
            if(e instanceof javax.ws.rs.WebApplicationException) {
                String message = ((WebApplicationException)e).getResponse().getStatusInfo().getReasonPhrase();
                int code = ((WebApplicationException)e).getResponse().getStatusInfo().getStatusCode();
                throw new APIException("[project_name : "+projectName+"] "+message, HttpStatus.resolve(code));
            }
            throw e;
        }

        Project project = makeProjectInfo(groupRepresentation);
        return project;
    }

    @Override
    public void updateProjectInfo(String projectName, Map<String,String> attrs) {
        String groupPath = this.projectNameToGroupPath(projectName);
        GroupRepresentation groupRepresentation = this.getGroupByGroupPath(groupPath);

        if(attrs != null){
            for(String key:attrs.keySet()){
                groupRepresentation.singleAttribute(key,attrs.get(key));
            }
        }

        this.updateGroup(groupRepresentation.getId(),groupRepresentation);

    }

    @Override
    public Boolean isOwnerOfProject(String userId,String projectName) {
        Boolean isOwner = false;
        String groupPath = this.projectNameToGroupPath(projectName);
        GroupRepresentation groupRepresentation = this.getGroupByGroupPath(groupPath);
        Project project = this.makeProjectInfo(groupRepresentation);
        if (project.getOwner().equals(userId)){
            isOwner = true;
        }
        return isOwner;
    }

    @Override
    public void deleteProjectById(String projectName) {
        String groupPath = this.projectNameToGroupPath(projectName);
        GroupRepresentation groupRepresentation = this.getGroupByGroupPath(groupPath);
        this.removeGroupById(groupRepresentation.getId());
    }

    @Override
    public List<Group> getGroupsByProjectName(String projectName) throws APIException{
        List<Group> groups = null;
        Project project = this.getProjectByProjectName(projectName);
        groups = project.getGroups();
        return groups;
    }

    @Override
    public void inviteUserToGroup(String email,String projectName,String groupName) throws KeyCloakAdminException{

        List<UserRepresentation> userRepresentations = this.getUserByEmail(email);

        //New User
        if(userRepresentations == null || userRepresentations.size() == 0){
            String groupPath = projectNameToGroupPath(projectName);
            String subGroupPath = groupPath+"/"+groupName;
            logger.debug("subGroupPath:"+subGroupPath);
            GroupRepresentation topGroup = this.getGroupByGroupPath(groupPath);
            GroupRepresentation subGroup = this.getGroupByGroupPath(subGroupPath);
            UserRepresentation userRepresentation = this.createUser(email);
            UserResource userResource = this.getUserResourceById(userRepresentation.getId());
            this.joinGroup(topGroup,userRepresentation.getId());
            this.joinGroup(subGroup,userRepresentation.getId());
            userResource.sendVerifyEmail();
        //Exists USer
        }else{

        }
    }

    @Override
    public void checkProjectByProjectName(String projectName) {
        String groupPath = this.projectNameToGroupPath(projectName);
        this.getGroupByGroupPath(groupPath);
    }


    private String projectNameToGroupPath(String projectName){
        String groupPath = "/"+projectName;
        return groupPath;
    }

}
