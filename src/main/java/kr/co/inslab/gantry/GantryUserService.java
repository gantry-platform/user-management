package kr.co.inslab.gantry;


import kr.co.inslab.keycloak.KeyCloakAdminException;
import kr.co.inslab.keycloak.AbstractKeyCloak;
import kr.co.inslab.model.Project;
import kr.co.inslab.model.User;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GantryUserService extends AbstractKeyCloak implements GantryUser {

    private final Logger logger = LoggerFactory.getLogger(GantryUserService.class);

    public GantryUserService(Keycloak keycloakAdmin) {
        super(keycloakAdmin);
    }


    @Override
    public User getUserInfoById(String userId,Boolean includeProject){
        UserResource userResource = this.getUserResourceById(userId);
        UserRepresentation gantryUser = userResource.toRepresentation();
        List<GroupRepresentation> gantryProjects = this.getGroupsByUserId(userId);

        User user = this.newUser(gantryUser);

        if (gantryProjects != null && gantryProjects.size() > 0){
            List<Project> projects = new ArrayList<>();
            for(GroupRepresentation gantryProject: gantryProjects){
                GroupRepresentation groupRepresentation = this.getGroupById(gantryProject.getId());
                Project project;
                if(includeProject){
                    project = this.makeProjectInfo(groupRepresentation);
                }else{
                    project = this.makeProjectMetaInfo(groupRepresentation);
                }
                if(project != null){
                    projects.add(project);
                }
            }
            user.setProjects(projects);
        }
        return user;
    }

    @Override
    public void checkUserById(String userId) throws UserException {
        try{
            super.checkUserById(userId);
        }catch (KeyCloakAdminException e){
            throw new UserException(e.getMessage(),e.getHttpStatus());
        }
    }

    private User newUser(UserRepresentation userRepresentation){
        User user = new User();
        user.setUserName(userRepresentation.getUsername());
        user.setFirstName(userRepresentation.getFirstName());
        user.setLastName(userRepresentation.getLastName());
        user.setEmailVerified(userRepresentation.isEmailVerified());
        user.setEmail(userRepresentation.getEmail());
        user.setUserId(userRepresentation.getId());
        return user;
    }

}
