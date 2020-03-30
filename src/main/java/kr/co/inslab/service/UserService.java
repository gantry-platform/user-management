package kr.co.inslab.service;

import kr.co.inslab.exception.ApiException;
import kr.co.inslab.exception.KeyCloakAdminException;
import kr.co.inslab.model.Project;
import kr.co.inslab.model.User;

public interface UserService {
    User getUserInfoById(String userId,Boolean includeProject);
    Project createProject(String userId, String displayName, String description) throws KeyCloakAdminException, ApiException;
    void checkUserById(String userId) throws ApiException;
}
