package kr.co.inslab.keycloak;

import kr.co.inslab.bootstrap.StaticConfig;
import kr.co.inslab.exception.KeyCloakAdminException;
import kr.co.inslab.model.*;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RoleResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public abstract class AbstractKeyCloak {

    private final Keycloak keycloakAdmin;

    @Value("${keycloak.targetRealm}")
    private String targetRealm;

    private static final Logger logger = LoggerFactory.getLogger(AbstractKeyCloak.class);


    public AbstractKeyCloak(Keycloak keycloakAdmin) {
        this.keycloakAdmin = keycloakAdmin;
    }


    protected List<UserRepresentation> getUserByEmail(String email){
        return this.getRealm().users().search(null,null,null,email,0,10);
    }

    protected UserResource getUserResourceById(String userId) {
        return this.getRealm().users().get(userId);
    }

    protected List<UserRepresentation> getMembersByGroupId(String groupId) {
        return this.getRealm().groups().group(groupId).members();
    }

    protected GroupRepresentation createGroup(String groupName, Map<String,String> groupAttr) throws KeyCloakAdminException {

        GroupRepresentation groupRepresentation = new GroupRepresentation();
        groupRepresentation.setName(groupName);
        if(groupAttr != null){
            for(String key: groupAttr.keySet()){
                groupRepresentation.singleAttribute(key,groupAttr.get(key));
            }
        }

        Response response = this.getRealm().groups().add(groupRepresentation);
        String createdId = getCreatedId(response,groupName);


        groupRepresentation.setId(createdId);
        return groupRepresentation;
    }

    protected GroupRepresentation addSubGroup(GroupRepresentation topGroup,String subGroupName) throws KeyCloakAdminException {

        GroupRepresentation groupRepresentation = new GroupRepresentation();
        groupRepresentation.setName(subGroupName);

        Response response = this.getRealm().groups().group(topGroup.getId()).subGroup(groupRepresentation);
        String createdId = getCreatedId(response,subGroupName);

        groupRepresentation.setId(createdId);
        return groupRepresentation;
    }

    protected void joinGroup(String userId,String groupId) {
        this.getRealm().users().get(userId).joinGroup(groupId);
    }

    protected void addRoleToGroup(GroupRepresentation targetGroup, Role role) {
        RoleResource roleResource = this.getRealm().roles().get(role.toString());
        RoleRepresentation roleRepresentation = roleResource.toRepresentation();
        List<RoleRepresentation> roleRepresentations = new ArrayList<>();
        roleRepresentations.add(roleRepresentation);
        this.getRealm().groups().group(targetGroup.getId()).roles().realmLevel().add(roleRepresentations);
    }

    protected void leaveGroup(String userId,String groupId) {
        this.getRealm().users().get(userId).leaveGroup(groupId);
    }

    protected GroupRepresentation getGroupById(String groupId) {
        return this.getRealm().groups().group(groupId).toRepresentation();
    }

    protected List<GroupRepresentation> getGroupsByUserId(String userId) {
        return this.getRealm().users().get(userId).groups();
    }

    protected void updateGroup(String groupId, GroupRepresentation groupRepresentation){
        this.getRealm().groups().group(groupId).update(groupRepresentation);
    }

    protected RealmResource getRealm(){
        return this.keycloakAdmin.realm(this.targetRealm);
    }

    protected void removeGroupById(String groupId){

        this.getRealm().groups().group(groupId).remove();
    }


    protected String getCreatedId(Response response,String resourceName) throws KeyCloakAdminException {
        URI location = response.getLocation();
        if (!response.getStatusInfo().equals(Response.Status.CREATED)) {
            Response.StatusType statusInfo = response.getStatusInfo();
            throw new KeyCloakAdminException("[resourceId : "+resourceName+"]"+statusInfo.getReasonPhrase(),HttpStatus.resolve(statusInfo.getStatusCode()));
        }
        if (location == null) {
            return null;
        }
        String path = location.getPath();
        return path.substring(path.lastIndexOf('/') + 1);
    }

    protected Project makeProjectInfo(GroupRepresentation groupRepresentation) {
        Project project = null;
        List<PendingUser> pendingUsers = this.makePendingUserFromEmailStatus(groupRepresentation.getId());
        List<GroupRepresentation> subGroups = groupRepresentation.getSubGroups();
        if (subGroups != null && subGroups.size() > 0) {
            project = new Project();
            project.setId(groupRepresentation.getId());
            project.setName(groupRepresentation.getName());
            this.setAdditionalProperties(groupRepresentation,project,pendingUsers);
            project.setGroups(this.addSubGroupsInfo(subGroups));
            project.setPendingUsers(pendingUsers);
        }
        return project;
    }


    protected Project makeProjectMetaInfo(GroupRepresentation groupRepresentation) {
        Project project = null;
        List<GroupRepresentation> subGroups = groupRepresentation.getSubGroups();
        if (subGroups != null && subGroups.size() > 0) {
            project = new Project();
            project.setId(groupRepresentation.getId());
            project.setName(groupRepresentation.getName());
            Map<String, List<String>> groupAttrs = groupRepresentation.getAttributes();

            if (groupAttrs != null) {
                for (String key : groupAttrs.keySet()) {
                    switch (key) {
                        case StaticConfig.DISPLAY_NAME:
                            project.setDisplayName(groupAttrs.get(key).get(0));
                            break;
                        case StaticConfig.OWNER:
                            project.setOwner(groupAttrs.get(key).get(0));
                            break;
                    }
                }
            }
        }
        return project;
    }
    private List<Group> addSubGroupsInfo(List<GroupRepresentation> subGroups) {

        List<Group> groups = new ArrayList<>();
        for (GroupRepresentation gantryGroup : subGroups) {
            Group group = new Group();
            group.setId(gantryGroup.getId());
            group.setName(gantryGroup.getName());
            group.setMembers(this.getMembers(gantryGroup.getId()));
            groups.add(group);
        }
        return groups;
    }

    private final List<Member> getMembers(String groupId) {
        List<Member> members = null;
        List<UserRepresentation> gantryMembers = this.getMembersByGroupId(groupId);
        if (gantryMembers.size() > 0) {
            members = new ArrayList<>();
            for (UserRepresentation gantryMember : gantryMembers) {
                Member member = new Member();
                member.setUserId(gantryMember.getId());
                member.setUserName(gantryMember.getUsername());
                member.setEmail(gantryMember.getEmail());
                member.setEmailVerified(gantryMember.isEmailVerified());
                members.add(member);
            }
        }
        return members;
    }


    private final List<PendingUser> makePendingUserFromEmailStatus(String groupId) {
        List<PendingUser> pendingUsers = null;
        List<UserRepresentation> gantryMembers = this.getMembersByGroupId(groupId);
        if (gantryMembers.size() > 0) {
            pendingUsers = new ArrayList<>();
            for (UserRepresentation gantryMember : gantryMembers) {
                if(!gantryMember.isEmailVerified()){
                    PendingUser pendingUser = new PendingUser();
                    pendingUser.setEmail(gantryMember.getEmail());
                    pendingUsers.add(pendingUser);
                }
            }
        }
        return pendingUsers;
    }



    private final void setAdditionalProperties(GroupRepresentation groupRepresentation,Project project,List<PendingUser> pendingUsers){
        Map<String, List<String>> groupAttrs = groupRepresentation.getAttributes();

        if (groupAttrs != null){
            for(String key : groupAttrs.keySet()){
                switch (key){
                    case StaticConfig.DESCRIPTION:
                        project.setDescription(groupAttrs.get(key).get(0));
                        break;
                    case StaticConfig.DISPLAY_NAME:
                        project.setDisplayName(groupAttrs.get(key).get(0));
                        break;
                    case StaticConfig.OWNER:
                        project.setOwner(groupAttrs.get(key).get(0));
                        break;
                    case StaticConfig.STATUS:
                        project.setStatus(Project.StatusEnum.fromValue(groupAttrs.get(key).get(0)));
                        break;
                    case StaticConfig.PENDING:
                        if(pendingUsers == null){
                            pendingUsers = new ArrayList<>();
                        }
                        List<String> pendingEmails = groupAttrs.get(key);
                        for(String email:pendingEmails){
                            PendingUser pendingUser = new PendingUser();
                            pendingUser.setEmail(email);
                            pendingUsers.add(pendingUser);
                        }
                        project.setPendingUsers(pendingUsers);
                        break;
                    default:
                        logger.warn("New Attr Key:"+key);
                }
            }
        }
    }
}
