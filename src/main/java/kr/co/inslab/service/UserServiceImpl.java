package kr.co.inslab.service;


import kr.co.inslab.keycloak.*;
import kr.co.inslab.model.Group;
import kr.co.inslab.model.Member;
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
public class UserServiceImpl extends AbstractKeyCloakAdminAPI implements UserService {

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl(KeyCloakAdmin keyCloakAdmin) {
        super(keyCloakAdmin);
    }



    @Override
    public Project createProject(String userId, String displayName, String description) throws Exception {
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

        GroupRepresentation projectGroupRep = super.createGroup(projectName,groupAttr);
        GroupRepresentation adminGroupRep = super.addSubGroup(projectGroupRep, adminGroupName);
        GroupRepresentation opsGroupRep = super.addSubGroup(projectGroupRep, opsGroupName);
        GroupRepresentation devGroupRep = super.addSubGroup(projectGroupRep, devGroupName);
        super.addRoleToGroup(adminGroupRep, Role.ADMIN);
        super.addRoleToGroup(opsGroupRep, Role.OPS);
        super.addRoleToGroup(devGroupRep, Role.DEV);
        super.joinGroup(projectGroupRep,userId);
        super.joinGroup(adminGroupRep,userId);


        Project project = new Project();
        project.setDisplayName(displayName);
        project.setName(projectName);
        project.setOwner(userId);

        if(!description.isEmpty()){
            project.setDescription(description);
        }
        List<Group> subGroups = new ArrayList<Group>();
        Group adminGroup = new Group();
        Group opsGroup = new Group();
        Group devGroup = new Group();

        List<Member> members = new ArrayList<Member>();
        Member member = new Member();
        member.setUserId(userId);
        members.add(member);

        adminGroup.setName(adminGroupName);
        adminGroup.setMembers(members);
        opsGroup.setName(opsGroupName);
        devGroup.setName(devGroupName);

        subGroups.add(adminGroup);
        subGroups.add(opsGroup);
        subGroups.add(devGroup);

        project.setGroups(subGroups);
        return project;
    }

    @Override
    public void checkUser(String userId) throws Exception {
        super.getUserResourceById(userId).toRepresentation();
    }

    @Override
    public User getUserInfoById(String userId) throws Exception {
        UserResource userResource = super.getUserResourceById(userId);
        UserRepresentation gantryUser = userResource.toRepresentation();
        List<GroupRepresentation> gantryProjects = super.getGroupsByUserId(userId);

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
                GroupRepresentation groupRepresentation = super.getGroupByGroupId(gantryProject.getId());
                List<GroupRepresentation> subGroups = groupRepresentation.getSubGroups();
                if(subGroups != null && subGroups.size() > 0){
                    Project project = new Project();
                    project.setName(groupRepresentation.getName());
                    Map<String, List<String>> groupAttrs = groupRepresentation.getAttributes();
                    if (groupAttrs != null){
                        for(String key : groupAttrs.keySet()){
                            switch (key){
                                case KeyCloakStaticConfig.DESCRIPTION:
                                    project.setDescription(groupAttrs.get(key).get(0));
                                    break;
                                case KeyCloakStaticConfig.DISPLAY_NAME:
                                    project.setDisplayName(groupAttrs.get(key).get(0));
                                    break;
                                case KeyCloakStaticConfig.OWNER:
                                    project.setOwner(groupAttrs.get(key).get(0));
                                    break;
                                default:
                                    logger.debug("attrs not found");
                            }
                        }
                    }

                    List<Group> groups = new ArrayList<Group>();
                    for (GroupRepresentation gantryGroup : subGroups) {
                        Group group = new Group();
                        group.setName(gantryGroup.getName());
                        List<UserRepresentation> gantryMembers = super.getMembersByGroupId(gantryGroup.getId());
                        if (gantryMembers.size() > 0) {
                            List<Member> members= new ArrayList<Member>();
                            for (UserRepresentation gantryMember : gantryMembers) {
                                Member member = new Member();
                                member.setUserId(gantryMember.getId());
                                member.setUserName(gantryMember.getUsername());
                                member.setEmail(gantryMember.getEmail());
                                members.add(member);
                            }
                            group.setMembers(members);
                        }
                        groups.add(group);
                    }
                    project.setGroups(groups);
                    projects.add(project);
                }
            }
            user.setProjects(projects);
        }
        return user;
    }



}
