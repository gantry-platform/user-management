package kr.co.inslab.gantry;

import kr.co.inslab.model.User;

public interface GantryUser {
    User getUserInfoById(String userId,Boolean includeProject);
    void checkUserById(String userId) throws UserException;
}
