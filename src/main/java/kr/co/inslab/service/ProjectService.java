package kr.co.inslab.service;

import kr.co.inslab.model.Project;

public interface ProjectService {
    public Boolean existsUserInProject(String userId,String projectName) throws Exception;
    public void checkUserById(String userId) throws Exception;
    public Project getProjectByProjectName(String projectName) throws Exception;
}
