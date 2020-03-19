package kr.co.inslab.service;

import kr.co.inslab.model.Project;

public interface ProjectService {
    public Boolean existsUserInProject(String userId,String projectName);
    public void checkUserById(String userId);
    public Project getProjectByProjectName(String projectName);
    public void updateProjectInfo(String projectName,String owner,String description);
    public Boolean isOwnerOfProject(String userId,String projectName);
}
