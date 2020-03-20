package kr.co.inslab.service;

import kr.co.inslab.exception.APIException;
import kr.co.inslab.exception.KeyCloakAdminException;
import kr.co.inslab.model.Group;
import kr.co.inslab.model.Project;

import java.util.List;
import java.util.Map;

public interface ProjectService {
    public Boolean existsUserInProject(String userId,String projectName);
    public void checkUserById(String userId) throws APIException;
    public Project getProjectByProjectName(String projectName) throws APIException;
    public void updateProjectInfo(String projectName, Map<String,String> attrs);
    public Boolean isOwnerOfProject(String userId,String projectName);
    public void deleteProjectById(String projectName);
    public List<Group> getGroupsByProjectName(String projectName) throws APIException;
    public void inviteUserToGroup(String email,String projectName,String groupName) throws KeyCloakAdminException;
    public void checkProjectByProjectName(String projectName);
}
