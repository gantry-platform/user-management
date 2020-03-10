package kr.co.inslab.service;


import kr.co.inslab.keycloak.KeyCloakAdmin;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersServiceImpl implements UserService {

    private final Logger logger = LoggerFactory.getLogger(UsersServiceImpl.class);


    private final KeyCloakAdmin keyCloakAdmin;

    public UsersServiceImpl(KeyCloakAdmin keyCloakAdmin){
        this.keyCloakAdmin = keyCloakAdmin;
    }

    @Override
    public List<UserRepresentation> getUserByEmail(String email) throws Exception {
        List<UserRepresentation> userRepresentations= this.keyCloakAdmin.getInstance().realm(this.keyCloakAdmin.getTargetRealm())
                .users().search(null,null,null,email,0,10);
        return userRepresentations;
    }

    @Override
    public UserResource getUserResourceById(String id) throws Exception {
        UserResource userResource = this.keyCloakAdmin.getInstance().realm(this.keyCloakAdmin.getTargetRealm()).users().get(id);
        return userResource;
    }

    @Override
    public void inviteUser(UserRepresentation userRepresentation) throws Exception {
        this.keyCloakAdmin.getInstance().realm(this.keyCloakAdmin.getTargetRealm()).users().create(userRepresentation);
    }
}
