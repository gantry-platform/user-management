package kr.co.inslab.service;

import kr.co.inslab.exception.APIException;
import kr.co.inslab.exception.KeyCloakAdminException;
import kr.co.inslab.model.Group;
import kr.co.inslab.model.Member;
import kr.co.inslab.model.Project;

import java.util.List;
import java.util.Map;

public interface ProjectService {
    public Boolean existsUserInProject(String userId,String projectId);
    public void checkUserById(String userId) throws APIException;
    public Project getProjectById(String projectId) throws APIException;
    public void updateProjectInfo(String projectId, Map<String,String> attrs);
    public Boolean isOwnerOfProject(String userId,String projectId);
    public void deleteProjectById(String projectId);
    public List<Group> getGroupsByProjectId(String projectId) throws APIException;
    public void inviteUserToGroup(String email,String projectId,String groupId) throws KeyCloakAdminException;
    public void checkProjectByProjectId(String projectId);
    public List<Member> getSubGroupMember(String projectId,String groupId);
}
