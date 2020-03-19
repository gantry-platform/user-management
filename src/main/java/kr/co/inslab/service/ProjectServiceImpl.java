package kr.co.inslab.service;

import kr.co.inslab.keycloak.AbstractKeyCloak;
import kr.co.inslab.keycloak.KeyCloakAdmin;
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
    public Boolean existsUserInProject(String userId, String projectName) throws Exception {
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
    public void checkUserById(String userId) throws Exception {
        this.getUserResourceById(userId).toRepresentation();
    }

    @Override
    public Project getProjectByProjectName(String projectName) throws Exception {
        String groupPath = "/"+projectName;
        GroupRepresentation groupRepresentation = this.getGroupByGroupPath(groupPath);
        Project project = makeProjectInfo(groupRepresentation.getId());
        return project;
    }

}
