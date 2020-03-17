package kr.co.inslab.service;

import kr.co.inslab.model.Project;
import kr.co.inslab.model.User;

public interface UserService {
    public User getUserInfoById(String userId) throws Exception;
    public Project createProject(String userId, String displayName, String description) throws Exception;
    public void checkUser(String userId) throws Exception;
}
