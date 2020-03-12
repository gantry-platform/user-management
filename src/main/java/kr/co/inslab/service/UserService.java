package kr.co.inslab.service;

import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;

public interface UserService {
    public List<UserRepresentation> getUserByEmail(String email) throws Exception;
    public UserResource getUserResourceById(String id) throws Exception;
    public void inviteUser(String email) throws Exception;

}
