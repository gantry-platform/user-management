package kr.co.inslab.keycloak;

import kr.co.inslab.exception.APIException;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public abstract class AbstractKeyCloakUser{

    private final KeyCloakAdmin keyCloakAdmin;

    public AbstractKeyCloakUser(KeyCloakAdmin keyCloakAdmin) {
        this.keyCloakAdmin = keyCloakAdmin;
    }


    public List<UserRepresentation> getUserByEmail(String email) throws APIException {
        List<UserRepresentation> userRepresentations= this.keyCloakAdmin.getInstance().realm(this.keyCloakAdmin.getTargetRealm())
                .users().search(null,null,null,email,0,10);
        return userRepresentations;
    }

    public UserResource getUserResourceById(String userId) throws APIException {
        UserResource userResource = this.keyCloakAdmin.getInstance().realm(this.keyCloakAdmin.getTargetRealm()).users().get(userId);
        return userResource;
    }

    public List<UserRepresentation> getMembersByGroupId(String groupId) throws APIException{
        List<UserRepresentation> userRepresentations = this.keyCloakAdmin.getInstance().realm(this.keyCloakAdmin.getTargetRealm()).groups().group(groupId).members();
        return userRepresentations;
    }

    public List<GroupRepresentation> getGroupsByUserId(String userId) throws APIException {
        List<GroupRepresentation> groupRepresentations = this.keyCloakAdmin.getInstance().realm(this.keyCloakAdmin.getTargetRealm()).users().get(userId).groups();
        return groupRepresentations;
    }

}
