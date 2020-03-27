package kr.co.inslab.service;

import kr.co.inslab.exception.ApiException;
import kr.co.inslab.exception.KeyCloakAdminException;
import kr.co.inslab.model.Group;
import kr.co.inslab.model.Member;
import kr.co.inslab.model.Project;

import java.util.List;
import java.util.Map;

public interface ProjectService {
    public Boolean existsUserInProject(String userId,String projectId);
    public void checkUserById(String userId) throws ApiException;
    public Project getProjectById(String projectId) throws ApiException;
    public void updateProjectInfo(String projectId, Map<String,String> attrs);
    public Boolean isOwnerOfProject(String userId,String projectId);
    public Boolean isAdminOfProject(String userId,String projectId);
    public void deleteProjectById(String projectId);
    public void deleteMemberInProject(String projectId,String memberId);
    public List<Group> getGroupsByProjectId(String projectId) throws ApiException;
    public void inviteUserToGroup(String email,String projectId,String groupId) throws KeyCloakAdminException, ApiException;
    public void checkProjectByProjectId(String projectId);
    public List<Member> getSubGroupMember(String projectId,String groupId);
    public void moveGroupOfMember(String projectId,String groupId,String memberId);
    public Boolean joinNewProjectAndGroupForExistsUser(String token);
}
