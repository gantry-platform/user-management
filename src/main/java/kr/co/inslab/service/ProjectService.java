package kr.co.inslab.service;

import kr.co.inslab.exception.ApiException;
import kr.co.inslab.exception.KeyCloakAdminException;
import kr.co.inslab.model.Group;
import kr.co.inslab.model.Member;
import kr.co.inslab.model.Project;

import java.util.List;
import java.util.Map;

public interface ProjectService {
     Boolean existsUserInProject(String userId,String projectId);
     void checkUserById(String userId) throws ApiException;
     Project getProjectById(String projectId) throws ApiException;
     void updateProjectInfo(String projectId, Map<String,String> attrs) throws ApiException;
     Boolean isOwnerOfProject(String userId,String projectId);
     Boolean isAdminOfProject(String userId,String projectId);
     void deleteProjectById(String projectId);
     void deleteMemberInProject(String projectId,String memberId);
     void deleteMemberInPending(String projectId,String email) throws ApiException;
     List<Group> getGroupsByProjectId(String projectId) throws ApiException;
     void inviteUserToGroup(String email,String projectId,String groupId) throws KeyCloakAdminException, ApiException;
     void checkProjectByProjectId(String projectId);
     List<Member> getSubGroupMember(String projectId,String groupId);
     void moveGroupOfMember(String projectId,String groupId,String memberId);
     Boolean joinNewProjectAndGroupForExistsUser(String token, String email) throws ApiException;
}
