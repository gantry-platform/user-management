package kr.co.inslab.service;


import kr.co.inslab.exception.APIException;
import kr.co.inslab.exception.KeyCloakAdminException;
import kr.co.inslab.keycloak.*;
import kr.co.inslab.model.Project;
import kr.co.inslab.model.User;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl extends AbstractKeyCloak implements UserService {

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl(KeyCloakAdmin keyCloakAdmin) {
        super(keyCloakAdmin);
    }


    @Override
    public Project createProject(String userId, String displayName, String description) throws KeyCloakAdminException{
        String projectName = userId+"_"+displayName;
        String adminGroupName = SubGroup.ADMIN.name().toLowerCase();
        String opsGroupName = SubGroup.OPS.name().toLowerCase();
        String devGroupName = SubGroup.DEV.name().toLowerCase();

        Map<String,String> groupAttr = new HashMap<String,String>();

        groupAttr.put(KeyCloakStaticConfig.DISPLAY_NAME,displayName);
        groupAttr.put(KeyCloakStaticConfig.OWNER,userId);

        if(!description.isEmpty()){
            groupAttr.put(KeyCloakStaticConfig.DESCRIPTION,description);
        }

        GroupRepresentation projectGroupRep = this.createGroup(projectName,groupAttr);
        GroupRepresentation adminGroupRep = this.addSubGroup(projectGroupRep, adminGroupName);
        GroupRepresentation opsGroupRep = this.addSubGroup(projectGroupRep, opsGroupName);
        GroupRepresentation devGroupRep = this.addSubGroup(projectGroupRep, devGroupName);
        this.addRoleToGroup(adminGroupRep, Role.ADMIN);
        this.addRoleToGroup(opsGroupRep, Role.OPS);
        this.addRoleToGroup(devGroupRep, Role.DEV);
        this.joinGroup(projectGroupRep,userId);
        this.joinGroup(adminGroupRep,userId);

        Project project = this.makeProjectInfo(projectGroupRep.getId());

        return project;
    }

    @Override
    public void checkUserById(String userId){
        this.getUserResourceById(userId).toRepresentation();
    }


    @Override
    public User getUserInfoById(String userId){
        UserResource userResource = this.getUserResourceById(userId);
        UserRepresentation gantryUser = userResource.toRepresentation();
        List<GroupRepresentation> gantryProjects = this.getGroupsByUserId(userId);

        User user = new User();
        user.setUserName(gantryUser.getUsername());
        user.setFirstName(gantryUser.getFirstName());
        user.setLastName(gantryUser.getLastName());
        user.setEmailVerified(gantryUser.isEmailVerified());
        user.setEmail(gantryUser.getEmail());
        user.setUserId(userId);

        if (gantryProjects != null && gantryProjects.size() > 0){
            List<Project> projects = new ArrayList<Project>();
            for(GroupRepresentation gantryProject: gantryProjects){
                Project project = super.makeProjectInfo(gantryProject.getId());
                if(project != null){
                    projects.add(project);
                }
            }
            user.setProjects(projects);
        }
        return user;
    }

}