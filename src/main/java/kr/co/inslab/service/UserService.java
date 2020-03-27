package kr.co.inslab.service;

import kr.co.inslab.exception.APIException;
import kr.co.inslab.exception.KeyCloakAdminException;
import kr.co.inslab.model.Project;
import kr.co.inslab.model.User;

public interface UserService {
    public User getUserInfoById(String userId,Boolean includeProject);
    public Project createProject(String userId, String displayName, String description) throws KeyCloakAdminException, APIException;
    public void checkUserById(String xUserId) throws APIException;
}
