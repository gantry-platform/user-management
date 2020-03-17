package kr.co.inslab.keycloak;

import kr.co.inslab.exception.KeyCloakAdminException;
import kr.co.inslab.model.Group;
import kr.co.inslab.model.Member;
import kr.co.inslab.model.Project;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RoleResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public abstract class AbstractKeyCloak {

    private final KeyCloakAdmin keyCloakAdmin;
    private static final Logger logger = LoggerFactory.getLogger(AbstractKeyCloak.class);

    public AbstractKeyCloak(KeyCloakAdmin keyCloakAdmin) {
        this.keyCloakAdmin = keyCloakAdmin;
    }


    protected List<UserRepresentation> getUserByEmail(String email) throws KeyCloakAdminException {
        List<UserRepresentation> userRepresentations= this.getRealm().users().search(null,null,null,email,0,10);
        return userRepresentations;
    }

    protected UserResource getUserResourceById(String userId) throws KeyCloakAdminException {
        UserResource userResource = this.getRealm().users().get(userId);
        return userResource;
    }

    protected List<UserRepresentation> getMembersByGroupId(String groupId) throws KeyCloakAdminException{
        List<UserRepresentation> userRepresentations = this.getRealm().groups().group(groupId).members();
        return userRepresentations;
    }

    protected GroupRepresentation createGroup(String groupName, Map<String,String> groupAttr) throws KeyCloakAdminException{

        GroupRepresentation groupRepresentation = new GroupRepresentation();
        groupRepresentation.setName(groupName);
        if(groupAttr != null){
            for(String key: groupAttr.keySet()){
                groupRepresentation.singleAttribute(key,groupAttr.get(key));
            }
        }

        Response response = this.getRealm().groups().add(groupRepresentation);
        String createdId = getCreatedId(response);
        logger.debug(createdId);

        groupRepresentation.setId(createdId);
        return groupRepresentation;
    }

    protected GroupRepresentation addSubGroup(GroupRepresentation topGroup,String subGroupName) throws KeyCloakAdminException{

        GroupRepresentation groupRepresentation = new GroupRepresentation();
        groupRepresentation.setName(subGroupName);

        Response response = this.getRealm().groups().group(topGroup.getId()).subGroup(groupRepresentation);
        String createdId = getCreatedId(response);

        groupRepresentation.setId(createdId);
        return groupRepresentation;
    }

    protected void joinGroup(GroupRepresentation joinGroup,String userId) throws KeyCloakAdminException{
        this.getRealm().users().get(userId).joinGroup(joinGroup.getId());
    }

    protected void addRoleToGroup(GroupRepresentation targetGroup,Role role) throws KeyCloakAdminException{
        RoleResource roleResource = this.getRealm().roles().get(role.name().toLowerCase());
        RoleRepresentation roleRepresentation = roleResource.toRepresentation();
        List<RoleRepresentation> roleRepresentations = new ArrayList<RoleRepresentation>();
        roleRepresentations.add(roleRepresentation);
        this.getRealm().groups().group(targetGroup.getId()).roles().realmLevel().add(roleRepresentations);
    }


    protected GroupRepresentation getGroupByGroupId(String groupId) throws KeyCloakAdminException{
        GroupRepresentation groupRepresentation = this.getRealm().groups().group(groupId).toRepresentation();
        return groupRepresentation;
    }

    protected List<GroupRepresentation> getGroupsByUserId(String userId) throws KeyCloakAdminException {
        List<GroupRepresentation> groupRepresentations = this.getRealm().users().get(userId).groups();
        return groupRepresentations;
    }

    protected RealmResource getRealm(){
        return this.keyCloakAdmin.getInstance().realm(this.keyCloakAdmin.getTargetRealm());
    }


    protected String getCreatedId(Response response) throws KeyCloakAdminException {
        URI location = response.getLocation();
        if (!response.getStatusInfo().equals(Response.Status.CREATED)) {
            Response.StatusType statusInfo = response.getStatusInfo();
            response.bufferEntity();
            String body = response.readEntity(String.class);
            throw new KeyCloakAdminException(statusInfo.toString(),HttpStatus.resolve(statusInfo.getStatusCode()));
        }
        if (location == null) {
            return null;
        }
        String path = location.getPath();
        return path.substring(path.lastIndexOf('/') + 1);
    }

    protected Project makeProjectInfo(String topGroupId) throws KeyCloakAdminException {
        Project project = null;
        GroupRepresentation groupRepresentation = this.getGroupByGroupId(topGroupId);
        List<GroupRepresentation> subGroups = groupRepresentation.getSubGroups();
        if (subGroups != null && subGroups.size() > 0) {
            project = new Project();
            project.setName(groupRepresentation.getName());
            this.setAdditionalProperties(groupRepresentation, project);
            project.setGroups(this.getSubGroups(subGroups));
        }
        return project;
    }

    private List<Group> getSubGroups(List<GroupRepresentation> subGroups) throws KeyCloakAdminException {

        List<Group> groups = new ArrayList<Group>();
        for (GroupRepresentation gantryGroup : subGroups) {
            Group group = new Group();
            group.setName(gantryGroup.getName());
            group.setMembers(this.getMembers(group,gantryGroup.getId()));
            groups.add(group);
        }
        return groups;
    }

    private List<Member> getMembers(Group group, String groupId) throws KeyCloakAdminException {
        List<Member> members = null;
        List<UserRepresentation> gantryMembers = this.getMembersByGroupId(groupId);
        if (gantryMembers.size() > 0) {
            members = new ArrayList<Member>();
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

    private void setAdditionalProperties(GroupRepresentation groupRepresentation,Project project){
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
                        logger.warn("New Attr Key:"+key);
                }
            }
        }
    }
}
