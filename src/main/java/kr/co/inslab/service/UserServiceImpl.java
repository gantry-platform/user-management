package kr.co.inslab.service;


import kr.co.inslab.bootstrap.KeyCloakAdminConfig;
import kr.co.inslab.bootstrap.StaticConfig;
import kr.co.inslab.exception.APIException;
import kr.co.inslab.exception.KeyCloakAdminException;
import kr.co.inslab.keycloak.*;
import kr.co.inslab.model.Project;
import kr.co.inslab.model.Role;
import kr.co.inslab.model.SubGroup;
import kr.co.inslab.model.User;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.ws.rs.WebApplicationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl extends AbstractKeyCloak implements UserService {

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl(KeyCloakAdminConfig keyCloakAdmin) {
        super(keyCloakAdmin);
    }


    @Override
    public Project createProject(String userId, String displayName, String description) throws KeyCloakAdminException, APIException {
        String projectName = userId+"_"+displayName;
        String adminGroupName = SubGroup.ADMIN.toString();
        String opsGroupName = SubGroup.OPS.toString();
        String devGroupName = SubGroup.DEV.toString();

        GroupRepresentation projectGroupRep = null;
        Map<String,String> groupAttr = new HashMap<String,String>();

        groupAttr.put(StaticConfig.DISPLAY_NAME,displayName);
        groupAttr.put(StaticConfig.OWNER,userId);
        groupAttr.put(StaticConfig.STATUS,Project.StatusEnum.ACTIVE.toString());

        if(!description.isEmpty()){
            groupAttr.put(StaticConfig.DESCRIPTION,description);
        }
        try{
            projectGroupRep = this.createGroup(projectName,groupAttr);
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
                throw new APIException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            if (e instanceof KeyCloakAdminException){
                throw e;
            }
        }

        Project project = new Project();
        project.setDisplayName(displayName);
        project.setName(projectName);
        project.setId(projectGroupRep.getId());

        return project;
    }

    @Override
    public void checkUserById(String userId) throws APIException{
        try{
            this.getUserResourceById(userId).toRepresentation();
        }catch (Exception e){
            if(e instanceof javax.ws.rs.WebApplicationException) {
                String message = ((WebApplicationException)e).getResponse().getStatusInfo().getReasonPhrase();
                int code = ((WebApplicationException)e).getResponse().getStatusInfo().getStatusCode();
                throw new APIException("[user_id : "+userId+"] "+ message, HttpStatus.resolve(code));
            }
            throw e;
        }
    }


    @Override
    public User getUserInfoById(String userId,Boolean includeProject){
        UserResource userResource = this.getUserResourceById(userId);
        UserRepresentation gantryUser = userResource.toRepresentation();
        List<GroupRepresentation> gantryProjects = this.getGroupsByUserId(userId);

        User user = this.newUser(gantryUser);

        if (gantryProjects != null && gantryProjects.size() > 0){
            List<Project> projects = new ArrayList<Project>();
            for(GroupRepresentation gantryProject: gantryProjects){
                GroupRepresentation groupRepresentation = this.getGroupById(gantryProject.getId());
                Project project = null;
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
