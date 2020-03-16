package kr.co.inslab.service;

import kr.co.inslab.model.User;

public interface UserService {
    public User getUserInfoById(String userId) throws Exception;

}
