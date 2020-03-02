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

    @Autowired
    KeyCloakAdmin keyCloakAdmin;



    @Override
    public List<UserRepresentation> getUserByEmail(String email) throws Exception {
        List<UserRepresentation> userRepresentations= keyCloakAdmin.getInstance().realm(keyCloakAdmin.getTargetRealm())
                .users().search(null,null,null,email,0,10);
        return userRepresentations;
    }

    @Override
    public UserResource getUserResourceById(String id) throws Exception {
        UserResource userResource = keyCloakAdmin.getInstance().realm(keyCloakAdmin.getTargetRealm()).users().get(id);
        return userResource;
    }

    @Override
    public void inviteUser(UserRepresentation userRepresentation) throws Exception {
        keyCloakAdmin.getInstance().realm(keyCloakAdmin.getTargetRealm()).users().create(userRepresentation);
    }
}
