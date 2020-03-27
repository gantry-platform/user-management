package kr.co.inslab.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.inslab.exception.APIException;
import kr.co.inslab.bootstrap.StaticConfig;
import kr.co.inslab.exception.KeyCloakAdminException;
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
    public String confirmJoin(WebRequest request,String token) {

        Boolean success = projectService.joinNewProjectAndGroupForExistsUser(token);

        //TODO: gantry url 변경

        if(success == false){
            return "redirect:http://127.0.0.1:80";
        }

        return "redirect:http://127.0.0.1:8080";
    }

    @Override
    public ResponseEntity<Void> projectsProjectIdActivePut(String xUserId, String projectId) throws APIException {
        ResponseEntity<Void> res = null;
        this.checkResource(xUserId,projectId);

        Map<String,String> attrs = new HashMap<String, String>();
        attrs.put(StaticConfig.STATUS,Project.StatusEnum.ACTIVE.toString());

        projectService.updateProjectInfo(projectId,attrs);

        res = new ResponseEntity<Void>(HttpStatus.OK);

        return res;
    }

    @Override
    public ResponseEntity<Void> projectsProjectIdArchivePut(String xUserId, String projectId) throws APIException {
        this.checkResource(xUserId,projectId);
        ResponseEntity<Void> res = null;
        Map<String,String> attrs = new HashMap<String, String>();

        attrs.put(StaticConfig.STATUS,Project.StatusEnum.ARCHIVE.toString());

        projectService.updateProjectInfo(projectId,attrs);

        res = new ResponseEntity<Void>(HttpStatus.OK);

        return res;
    }

    @Override
    public ResponseEntity<Void> projectsProjectIdDelete(String xUserId, String projectId) throws APIException {
        ResponseEntity<Void> res = null;

        this.checkResource(xUserId,projectId);

        if(!projectService.isOwnerOfProject(xUserId,projectId)){
            throw new APIException(xUserId+"is not the owner of project",HttpStatus.BAD_REQUEST);
        }

        projectService.deleteProjectById(projectId);

        res = new ResponseEntity<Void>(HttpStatus.OK);

        return res;
    }

    @Override
    public ResponseEntity<Project> projectsProjectIdGet(String xUserId, String projectId) throws APIException {
        ResponseEntity<Project> res = null;
        this.checkResource(xUserId,projectId);

        Boolean existsUserInProject = projectService.existsUserInProject(xUserId,projectId);

        if(!existsUserInProject){
            throw new APIException("Does Not Exists User In Project",HttpStatus.BAD_REQUEST);
        }

        Project project = projectService.getProjectById(projectId);
        res = new ResponseEntity<Project>(project,HttpStatus.OK);
        return res;
    }

    @Override
    public ResponseEntity<List<Group>> projectsProjectIdGroupsGet(String xUserId, String projectId) throws APIException {
        ResponseEntity<List<Group>> res = null;

        this.checkResource(xUserId,projectId);

        List<Group> groups = projectService.getGroupsByProjectId(projectId);

        res = new ResponseEntity<List<Group>>(groups,HttpStatus.OK);
        return res;
    }

    @Override
    public ResponseEntity<Void> projectsProjectIdGroupsGroupIdInvitationPut(String xUserId, String projectId, String groupId, @NotNull @Valid String email) throws APIException, KeyCloakAdminException {
        ResponseEntity<Void> res = null;

        this.checkResource(xUserId,projectId);
        projectService.inviteUserToGroup(email,projectId,groupId);
        res = new ResponseEntity<Void>(HttpStatus.OK);

        return res;
    }

    @Override
    public ResponseEntity<List<Member>> projectsProjectIdGroupsGroupIdMembersGet(String xUserId, String projectId, String groupId) throws APIException {
        ResponseEntity<List<Member>> res = null;
        this.checkResource(xUserId,projectId);
        List<Member> members = projectService.getSubGroupMember(projectId,groupId);
        res = new ResponseEntity<List<Member>>(members,HttpStatus.OK);
        return res;
    }

    @Override
    public ResponseEntity<Void> projectsProjectIdGroupsGroupIdMembersPatch(String xUserId, String projectId, String groupId, @NotNull @Valid String memberId) throws APIException {
        ResponseEntity<Void> res = null;
        this.checkResource(xUserId,projectId);

        if(!projectService.isOwnerOfProject(xUserId,projectId)){
            throw new APIException(xUserId+"is not the owner of project",HttpStatus.BAD_REQUEST);
        }

        projectService.moveGroupOfMember(projectId,groupId,memberId);
        res = new ResponseEntity<Void>(HttpStatus.OK);

        return res;
    }

    @Override
    public ResponseEntity<Void> projectsProjectIdMembersMemberIdDelete(String xUserId, String projectId, String memberId) throws APIException {
        ResponseEntity<Void> res = null;

        this.checkResource(xUserId,projectId);

        if(!projectService.isOwnerOfProject(xUserId,projectId)){
            throw new APIException(xUserId+"is not the owner of project",HttpStatus.BAD_REQUEST);
        }
        projectService.deleteProjectById(projectId);

        res = new ResponseEntity<Void>(HttpStatus.OK);

        return res;
    }

    @Override
    public ResponseEntity<Void> projectsProjectIdPatch(@Valid UpdateProject body, String xUserId, String projectId) throws APIException {
        ResponseEntity<Void> res = null;
        Map<String,String> attrs = null;
        String owner = body.getOwner();
        String description = body.getDescription();

        this.checkResource(xUserId,projectId);

        if(!projectService.isOwnerOfProject(xUserId,projectId)){
            throw new APIException(xUserId+"is not the owner of project",HttpStatus.BAD_REQUEST);
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
}
