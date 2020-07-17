package kr.co.inslab.gantry;


import kr.co.inslab.keycloak.AbstractKeyCloak;
import kr.co.inslab.keycloak.KeyCloakAdminException;
import kr.co.inslab.model.NewUser;
import kr.co.inslab.model.UpdateUser;
import kr.co.inslab.model.User;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
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
    public User getUserInfoById(String userId){
        UserResource userResource = this.getUserResourceById(userId);
        UserRepresentation gantryUser = userResource.toRepresentation();
        return this.setGantryUser(gantryUser);
    }

    @Override
    public void updateUser(String userId, UpdateUser updateUser) throws UserException {

        UserRepresentation gantryUser = this.getUserRepresentation(userId);

        if(updateUser.getFirstName() != null){
            gantryUser.setFirstName(updateUser.getFirstName());
        }
        if(updateUser.getLastName() != null){
            gantryUser.setLastName(updateUser.getLastName());
        }

        super.updateUser(userId,gantryUser);

    }

    @Override
    public void disableUser(String userId) throws UserException {
        UserRepresentation gantryUser = this.getUserRepresentation(userId);
        gantryUser.setEnabled(false);

        super.updateUser(userId,gantryUser);
        this.deleteUserInGroup(userId);
    }


    @Override
    public void checkUserById(String userId) throws UserException {
        try{
            super.checkUserById(userId);
        }catch (KeyCloakAdminException e){
            throw new UserException(e.getMessage(),e.getHttpStatus());
        }
    }

    @Override
    public void deleteUser(String userId){
        super.removeUser(userId);
    }

    //for test
    @Override
    public void createTestUser(NewUser newUser){
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setEmailVerified(true);
        userRepresentation.setEnabled(true);
        userRepresentation.setEmail(newUser.getEmail());
        userRepresentation.setUsername(newUser.getUserName());

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(newUser.getPassword());
        credential.setTemporary(false);

        List<CredentialRepresentation> credentialRepresentations = new ArrayList<>();
        credentialRepresentations.add(credential);
        userRepresentation.setCredentials(credentialRepresentations);
        super.createTestUser(userRepresentation);

    }

    private void deleteUserInGroup(String userId){
        List<GroupRepresentation> userGroups = super.getGroupsByUserId(userId);

        for(GroupRepresentation userGroup: userGroups){
            super.leaveGroup(userId,userGroup.getId());
        }
    }

    private User setGantryUser(UserRepresentation userRepresentation){
        User user = new User();
        user.setUserName(userRepresentation.getUsername());
        user.setFirstName(userRepresentation.getFirstName());
        user.setLastName(userRepresentation.getLastName());
        user.setEmailVerified(userRepresentation.isEmailVerified());
        user.setEmail(userRepresentation.getEmail());
        return user;
    }

    private UserRepresentation getUserRepresentation(String userId){
        UserResource userResource = super.getUserResourceById(userId);
        return userResource.toRepresentation();
    }
}
