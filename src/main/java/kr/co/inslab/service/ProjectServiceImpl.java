package kr.co.inslab.service;


import kr.co.inslab.keycloak.AbstractKeyCloak;
import kr.co.inslab.keycloak.KeyCloakAdmin;
import kr.co.inslab.keycloak.KeyCloakStaticConfig;
import kr.co.inslab.model.Project;
import org.keycloak.representations.idm.GroupRepresentation;
import org.springframework.stereotype.Service;

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
    public void checkUserById(String userId){

        this.getUserResourceById(userId).toRepresentation();
    }


    @Override
    public Project getProjectByProjectName(String projectName){
        String groupPath = this.projectNameToGroupPath(projectName);
        GroupRepresentation groupRepresentation = this.getGroupByGroupPath(groupPath);
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

    private String projectNameToGroupPath(String projectName){
        String groupPath = "/"+projectName;
        return groupPath;
    }

}
