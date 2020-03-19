package kr.co.inslab.service;


import kr.co.inslab.exception.APIException;
import kr.co.inslab.keycloak.AbstractKeyCloak;
import kr.co.inslab.keycloak.KeyCloakAdmin;
import kr.co.inslab.keycloak.KeyCloakStaticConfig;
import kr.co.inslab.model.Project;
import org.keycloak.representations.idm.GroupRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.ws.rs.WebApplicationException;
import java.util.List;

@Service
public class ProjectServiceImpl extends AbstractKeyCloak implements ProjectService {

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

        Project project = makeProjectInfo(groupRepresentation.getId());
        return project;
    }

    @Override
    public void updateProjectInfo(String projectName,String owner, String description) {
        String groupPath = this.projectNameToGroupPath(projectName);
        GroupRepresentation groupRepresentation = this.getGroupByGroupPath(groupPath);
        if(!owner.isEmpty()){
            groupRepresentation.singleAttribute(KeyCloakStaticConfig.OWNER,owner);
        }
        if(!description.isEmpty()){
            groupRepresentation.singleAttribute(KeyCloakStaticConfig.DESCRIPTION,description);
        }

        this.updateGroup(groupRepresentation.getId(),groupRepresentation);

    }

    @Override
    public Boolean isOwnerOfProject(String userId,String projectName) {
        Boolean isOwner = false;
        String groupPath = this.projectNameToGroupPath(projectName);
        GroupRepresentation groupRepresentation = this.getGroupByGroupPath(groupPath);
        Project project = this.makeProjectInfo(groupRepresentation.getId());
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

    private String projectNameToGroupPath(String projectName){
        String groupPath = "/"+projectName;
        return groupPath;
    }

}
