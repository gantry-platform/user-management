package kr.co.inslab.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.inslab.exception.APIException;
import kr.co.inslab.bootstrap.StaticConfig;
import kr.co.inslab.model.Group;
import kr.co.inslab.model.Member;
import kr.co.inslab.model.Project;
import kr.co.inslab.model.UpdateProject;
import kr.co.inslab.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-03-16T10:39:54.886+09:00[Asia/Seoul]")
@Controller
public class ProjectsApiController implements ProjectsApi {

    private static final Logger log = LoggerFactory.getLogger(ProjectsApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    private final ProjectService projectService;

    @org.springframework.beans.factory.annotation.Autowired
    public ProjectsApiController(ObjectMapper objectMapper, HttpServletRequest request, ProjectService projectService) {
        this.objectMapper = objectMapper;
        this.request = request;
        this.projectService = projectService;
    }

    //TODO: interceptor나 adviser로변경
    private void checkResource(String userId,String projectId) throws APIException {
        projectService.checkUserById(userId);
        projectService.checkProjectByProjectId(projectId);
    }

    @Override
    public ResponseEntity<Void> userIdProjectsProjectIdActivePut(String userId, String projectId) throws Exception {
        ResponseEntity<Void> res = null;
        this.checkResource(userId,projectId);

        Map<String,String> attrs = new HashMap<String, String>();
        attrs.put(StaticConfig.STATUS,Project.StatusEnum.ACTIVE.toString());

        projectService.updateProjectInfo(projectId,attrs);

        res = new ResponseEntity<Void>(HttpStatus.OK);

        return res;
    }

    @Override
    public ResponseEntity<Void> userIdProjectsProjectIdArchivePut(String userId, String projectId) throws Exception {
        this.checkResource(userId,projectId);
        ResponseEntity<Void> res = null;
        Map<String,String> attrs = new HashMap<String, String>();

        attrs.put(StaticConfig.STATUS,Project.StatusEnum.ARCHIVE.toString());

        projectService.updateProjectInfo(projectId,attrs);

        res = new ResponseEntity<Void>(HttpStatus.OK);

        return res;
    }

    @Override
    public ResponseEntity<Void> userIdProjectsProjectIdDelete(String userId, String projectId) throws Exception {
        ResponseEntity<Void> res = null;

        this.checkResource(userId,projectId);

        if(!projectService.isOwnerOfProject(userId,projectId)){
            throw new APIException(userId+"is not the owner of project",HttpStatus.BAD_REQUEST);
        }

        projectService.deleteProjectById(projectId);

        res = new ResponseEntity<Void>(HttpStatus.OK);

        return res;
    }

    @Override
    public ResponseEntity<Project> userIdProjectsProjectIdGet(String userId, String projectId) throws Exception {
        ResponseEntity<Project> res = null;
        this.checkResource(userId,projectId);

        Boolean existsUserInProject = projectService.existsUserInProject(userId,projectId);

        if(!existsUserInProject){
            throw new APIException("Does Not Exists User In Project",HttpStatus.BAD_REQUEST);
        }

        Project project = projectService.getProjectById(projectId);
        res = new ResponseEntity<Project>(project,HttpStatus.OK);
        return res;
    }

    @Override
    public ResponseEntity<List<Group>> userIdProjectsProjectIdGroupsGet(String userId, String projectId) throws Exception {
        ResponseEntity<List<Group>> res = null;

        this.checkResource(userId,projectId);

        List<Group> groups = projectService.getGroupsByProjectId(projectId);

        res = new ResponseEntity<List<Group>>(groups,HttpStatus.OK);
        return res;
    }

    @Override
    public ResponseEntity<Void> userIdProjectsProjectIdGroupsGroupIdInvitationPut(String userId, String projectId, String groupId, @NotNull @Valid String email) throws Exception {
        ResponseEntity<Void> res = null;

        this.checkResource(userId,projectId);
        projectService.inviteUserToGroup(email,projectId,groupId);
        res = new ResponseEntity<Void>(HttpStatus.OK);

        return res;
    }

    @Override
    public ResponseEntity<List<Member>> userIdProjectsProjectIdGroupsGroupIdMembersGet(String userId, String projectId, String groupId) throws Exception {
        ResponseEntity<List<Member>> res = null;
        this.checkResource(userId,projectId);
        List<Member> members = projectService.getSubGroupMember(projectId,groupId);
        res = new ResponseEntity<List<Member>>(members,HttpStatus.OK);
        return res;
    }

    @Override
    public ResponseEntity<Void> userIdProjectsProjectIdGroupsGroupIdMembersPatch(String userId, String projectId, String groupId, @NotNull @Valid String memberId) throws Exception {
        ResponseEntity<Void> res = null;
        this.checkResource(userId,projectId);

        if(!projectService.isOwnerOfProject(userId,projectId)){
            throw new APIException(userId+"is not the owner of project",HttpStatus.BAD_REQUEST);
        }

        projectService.moveGroupOfMember(projectId,groupId,memberId);
        res = new ResponseEntity<Void>(HttpStatus.OK);

        return res;
    }

    @Override
    public ResponseEntity<Void> userIdProjectsProjectIdMembersMemberIdDelete(String userId, String projectId, String memberId) throws Exception {
        ResponseEntity<Void> res = null;

        this.checkResource(userId,projectId);

        if(!projectService.isOwnerOfProject(userId,projectId)){
            throw new APIException(userId+"is not the owner of project",HttpStatus.BAD_REQUEST);
        }
        projectService.deleteProjectById(projectId);

        res = new ResponseEntity<Void>(HttpStatus.OK);

        return res;
    }

    @Override
    public ResponseEntity<Void> userIdProjectsProjectIdPatch(@Valid UpdateProject body, String userId, String projectId) throws Exception {
        ResponseEntity<Void> res = null;
        Map<String,String> attrs = null;
        String owner = body.getOwner();
        String description = body.getDescription();

        this.checkResource(userId,projectId);

        if(!projectService.isOwnerOfProject(userId,projectId)){
            throw new APIException(userId+"is not the owner of project",HttpStatus.BAD_REQUEST);
        }

        if(!owner.isEmpty() || !description.isEmpty()){
            attrs = new HashMap<String, String>();
            if(!owner.isEmpty()){
                attrs.put(StaticConfig.OWNER,owner);
            }
            if(!description.isEmpty()){
                attrs.put(StaticConfig.DESCRIPTION,description);
            }
        }

        projectService.updateProjectInfo(projectId,attrs);

        res = new ResponseEntity<Void>(HttpStatus.OK);

        return res;
    }

    @Override
    public String confirmJoin(WebRequest request,String token) {

        Boolean success = projectService.joinNewProjectAndGroupForExistsUser(token);

        //TODO: gantry url 변경

        if(success == false){
            return "redirect:http://127.0.0.1:80";
        }

        return "redirect:http://127.0.0.1:8080";
    }
}
