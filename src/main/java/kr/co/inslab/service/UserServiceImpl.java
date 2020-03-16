package kr.co.inslab.service;


import kr.co.inslab.keycloak.AbstractKeyCloakUser;
import kr.co.inslab.keycloak.KeyCloakAdmin;
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
import java.util.List;

@Service
public class UserServiceImpl extends AbstractKeyCloakUser implements UserService {

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl(KeyCloakAdmin keyCloakAdmin) {
        super(keyCloakAdmin);
    }


    @Override
    public User getUserInfoById(String userId) throws Exception {
        UserResource userResource = super.getUserResourceById(userId);
        UserRepresentation gantryUser = userResource.toRepresentation();
        List<GroupRepresentation> gantryProjects =super.getGroupsByUserId(userId);

        User user = new User();
        user.setUserName(gantryUser.getUsername());
        user.setFirstName(gantryUser.getFirstName());
        user.setLastName(gantryUser.getLastName());
        user.setEmailVerified(gantryUser.isEmailVerified());
        user.setUserId(userId);

        //TODO : attrs 추가해야함 .(owner,project_display_name)
        if (gantryProjects.size() > 0){
            List<Project> projects = new ArrayList<Project>();
            for(GroupRepresentation gantryProject: gantryProjects){
                Project project = new Project();
                project.setName(gantryProject.getName());
                List<GroupRepresentation> gantryGroups = gantryProject.getSubGroups();
                if(gantryGroups != null && gantryGroups.size() > 0) {
                    List<Group> groups = new ArrayList<Group>();
                    for (GroupRepresentation gantryGroup : gantryGroups) {
                        Group group = new Group();
                        group.setName(gantryGroup.getName());
                        List<UserRepresentation> gantryMembers = super.getMembersByGroupId(gantryGroup.getName());
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
                }
                projects.add(project);
            }
            user.setProjects(projects);
        }
        return user;
    }
}
