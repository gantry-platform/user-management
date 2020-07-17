package kr.co.inslab.gantry;

import kr.co.inslab.model.NewUser;
import kr.co.inslab.model.UpdateUser;
import kr.co.inslab.model.User;

public interface GantryUser {
    User getUserInfoById(String userId) throws UserException;
    void updateUser(String userId, UpdateUser updateUser) throws UserException;
    void disableUser(String userId) throws UserException;
    default void deleteUser(String userId) throws Exception{
    }
    default void createTesUser(NewUser newUser) throws Exception{

    }
    default void checkUserById(String userId) throws UserException {

    }
}
