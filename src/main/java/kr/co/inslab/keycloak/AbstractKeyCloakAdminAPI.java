package kr.co.inslab.keycloak;

import kr.co.inslab.exception.KeyCloakAdminException;
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
public abstract class AbstractKeyCloakAdminAPI {

    private final KeyCloakAdmin keyCloakAdmin;
    private static final Logger logger = LoggerFactory.getLogger(AbstractKeyCloakAdminAPI.class);

    public AbstractKeyCloakAdminAPI(KeyCloakAdmin keyCloakAdmin) {
        this.keyCloakAdmin = keyCloakAdmin;
    }


    public List<UserRepresentation> getUserByEmail(String email) throws KeyCloakAdminException {
        List<UserRepresentation> userRepresentations= this.getRealm().users().search(null,null,null,email,0,10);
        return userRepresentations;
    }

    public UserResource getUserResourceById(String userId) throws KeyCloakAdminException {
        UserResource userResource = this.getRealm().users().get(userId);
        return userResource;
    }

    public List<UserRepresentation> getMembersByGroupId(String groupId) throws KeyCloakAdminException{
        List<UserRepresentation> userRepresentations = this.getRealm().groups().group(groupId).members();
        return userRepresentations;
    }

    public GroupRepresentation createGroup(String groupName, Map<String,String> groupAttr) throws KeyCloakAdminException{

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

    public GroupRepresentation addSubGroup(GroupRepresentation topGroup,String subGroupName) throws KeyCloakAdminException{

        GroupRepresentation groupRepresentation = new GroupRepresentation();
        groupRepresentation.setName(subGroupName);

        Response response = this.getRealm().groups().group(topGroup.getId()).subGroup(groupRepresentation);
        String createdId = getCreatedId(response);

        groupRepresentation.setId(createdId);
        return groupRepresentation;
    }

    public void joinGroup(GroupRepresentation joinGroup,String userId) throws KeyCloakAdminException{
        this.getRealm().users().get(userId).joinGroup(joinGroup.getId());
    }

    public void addRoleToGroup(GroupRepresentation targetGroup,Role role) throws KeyCloakAdminException{
        RoleResource roleResource = this.getRealm().roles().get(role.name().toLowerCase());
        RoleRepresentation roleRepresentation = roleResource.toRepresentation();
        List<RoleRepresentation> roleRepresentations = new ArrayList<RoleRepresentation>();
        roleRepresentations.add(roleRepresentation);
        this.getRealm().groups().group(targetGroup.getId()).roles().realmLevel().add(roleRepresentations);
    }


    public GroupRepresentation getGroupByGroupId(String groupId) throws KeyCloakAdminException{
        GroupRepresentation groupRepresentation = this.getRealm().groups().group(groupId).toRepresentation();
        return groupRepresentation;
    }

    public List<GroupRepresentation> getGroupsByUserId(String userId) throws KeyCloakAdminException {
        List<GroupRepresentation> groupRepresentations = this.getRealm().users().get(userId).groups();
        return groupRepresentations;
    }

    private RealmResource getRealm(){
        return this.keyCloakAdmin.getInstance().realm(this.keyCloakAdmin.getTargetRealm());
    }


    private String getCreatedId(Response response) throws KeyCloakAdminException {
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
}
