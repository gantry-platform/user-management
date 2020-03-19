package kr.co.inslab.service;

import kr.co.inslab.exception.KeyCloakAdminException;
import kr.co.inslab.model.Project;
import kr.co.inslab.model.User;

public interface UserService {
    public User getUserInfoById(String userId);
    public Project createProject(String userId, String displayName, String description) throws KeyCloakAdminException;
    public void checkUserById(String userId);
}
