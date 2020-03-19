package kr.co.inslab.service;

import kr.co.inslab.exception.APIException;
import kr.co.inslab.model.Project;

import java.util.Map;

public interface ProjectService {
    public Boolean existsUserInProject(String userId,String projectName);
    public void checkUserById(String userId) throws APIException;
    public Project getProjectByProjectName(String projectName) throws APIException;
    public void updateProjectInfo(String projectName, Map<String,String> attrs);
    public Boolean isOwnerOfProject(String userId,String projectName);
    public void deleteProjectById(String projectName);
}
