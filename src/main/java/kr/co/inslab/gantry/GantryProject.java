package kr.co.inslab.gantry;

import kr.co.inslab.model.Group;
import kr.co.inslab.model.Member;
import kr.co.inslab.model.Project;

import java.util.List;
import java.util.Map;

public interface GantryProject {
     Project createProject(String userId, String displayName, String description) throws ProjectException;
     Boolean existsUserInProject(String userId,String projectId);
     Project getProjectById(String projectId) throws ProjectException;
     void updateProjectInfo(String projectId, Map<String,String> attrs) throws ProjectException;
     Boolean isOwnerOfProject(String userId,String projectId);
     Boolean isAdminOfProject(String userId,String projectId);
     void deleteProjectById(String projectId);
     void deleteMemberInProject(String projectId,String memberId);
     void deleteMemberInPending(String projectId,String email) throws ProjectException;
     List<Group> getGroupsByProjectId(String projectId) throws ProjectException;
     void inviteUserToGroup(String email,String projectId,String groupId) throws ProjectException;
     void checkProjectByProjectId(String projectId);
     List<Member> getSubGroupMember(String projectId,String groupId);
     void moveGroupOfMember(String projectId,String groupId,String memberId);
     Boolean joinNewProjectAndGroupForExistsUser(String mailToken, String email) throws ProjectException;

}
