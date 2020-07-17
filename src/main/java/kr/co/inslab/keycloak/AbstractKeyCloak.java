package kr.co.inslab.keycloak;

import kr.co.inslab.gantry.UserException;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;
import java.util.Map;

@Component
public abstract class AbstractKeyCloak {

    @Value("${keycloak.targetRealm}")
    private String targetRealm;

    private final Keycloak keycloakAdmin;

    private static final Logger logger = LoggerFactory.getLogger(AbstractKeyCloak.class);

    public AbstractKeyCloak(Keycloak keycloakAdmin) {
        this.keycloakAdmin = keycloakAdmin;
    }

    protected List<UserRepresentation> getUserByEmail(String email) {
        return this.getRealm().users().search(null, null, null, email, 0, 10);
    }

    protected UserResource getUserResourceById(String userId) {
        return this.getRealm().users().get(userId);
    }

    protected List<UserRepresentation> getMembersByGroupId(String groupId) {
        return this.getRealm().groups().group(groupId).members();
    }

    protected GroupRepresentation createGroup(String groupName, Map<String, String> groupAttr) throws KeyCloakAdminException {
        GroupRepresentation groupRepresentation = new GroupRepresentation();
        groupRepresentation.setName(groupName);

        if (groupAttr != null) {
            for (String key : groupAttr.keySet()) {
                groupRepresentation.singleAttribute(key, groupAttr.get(key));
            }
        }

        Response response = this.getRealm().groups().add(groupRepresentation);
        String createdId = getCreatedId(response, groupName);
        groupRepresentation.setId(createdId);

        return groupRepresentation;
    }

    protected GroupRepresentation addSubGroup(GroupRepresentation topGroup, String subGroupName) throws KeyCloakAdminException {
        GroupRepresentation groupRepresentation = new GroupRepresentation();
        groupRepresentation.setName(subGroupName);

        Response response = this.getRealm().groups().group(topGroup.getId()).subGroup(groupRepresentation);
        String createdId = getCreatedId(response, subGroupName);
        groupRepresentation.setId(createdId);

        return groupRepresentation;
    }

    protected void joinGroup(String userId, String groupId) {
        this.getRealm().users().get(userId).joinGroup(groupId);
    }


    protected void leaveGroup(String userId, String groupId) {
        this.getRealm().users().get(userId).leaveGroup(groupId);
    }

    protected  void updateUser(String userId,UserRepresentation userRepresentation){
        this.getRealm().users().get(userId).update(userRepresentation);
    }

    protected GroupRepresentation getGroupById(String groupId) {
        return this.getRealm().groups().group(groupId).toRepresentation();
    }

    protected List<GroupRepresentation> getGroupsByUserId(String userId) {
        return this.getRealm().users().get(userId).groups();
    }

    protected void updateGroup(String groupId, GroupRepresentation groupRepresentation) {
        this.getRealm().groups().group(groupId).update(groupRepresentation);
    }

    protected RealmResource getRealm() {
        return this.keycloakAdmin.realm(this.targetRealm);
    }

    protected void removeGroupById(String groupId) {
        this.getRealm().groups().group(groupId).remove();
    }

    protected void removeUser(String userId) {
        this.getRealm().users().get(userId).remove();
    }

    protected String getCreatedId(Response response, String resourceName) throws KeyCloakAdminException {
        URI location = response.getLocation();
        if (!response.getStatusInfo().equals(Response.Status.CREATED)) {
            Response.StatusType statusInfo = response.getStatusInfo();
            throw new KeyCloakAdminException("[resourceId : " + resourceName + "]" + statusInfo.getReasonPhrase(), HttpStatus.resolve(statusInfo.getStatusCode()));
        }
        if (location == null) {
            return null;
        }
        String path = location.getPath();
        return path.substring(path.lastIndexOf('/') + 1);
    }

    protected void createTestUser(UserRepresentation userRepresentation){
        this.getRealm().users().create(userRepresentation);
    }

    protected void checkUserById(String userId) throws KeyCloakAdminException, UserException {
        try {
            this.getUserResourceById(userId).toRepresentation();
        } catch (Exception e) {
            if (e instanceof javax.ws.rs.WebApplicationException) {
                String message = ((WebApplicationException) e).getResponse().getStatusInfo().getReasonPhrase();
                int code = ((WebApplicationException) e).getResponse().getStatusInfo().getStatusCode();
                throw new KeyCloakAdminException("[user_id : " + userId + "] " + message, HttpStatus.resolve(code));
            }
            throw e;
        }
    }
}
