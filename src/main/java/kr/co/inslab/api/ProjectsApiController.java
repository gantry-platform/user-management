package kr.co.inslab.api;

import kr.co.inslab.exception.APIException;
import kr.co.inslab.exception.KeyCloakAdminException;
import kr.co.inslab.keycloak.KeyCloakStaticConfig;
import kr.co.inslab.model.Group;
import kr.co.inslab.model.Member;
import kr.co.inslab.model.Project;
import kr.co.inslab.model.UpdateProject;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import kr.co.inslab.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import java.io.IOException;
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

    public ResponseEntity<Void> userIdProjectsProjectNameActivePut(@ApiParam(value = "user id (not name or email)",required=true) @PathVariable("user_id") String userId
,@ApiParam(value = "project name",required=true) @PathVariable("project_name") String projectName) throws Exception{
        ResponseEntity<Void> res = null;
        this.checkResource(userId,projectName);

        Map<String,String> attrs = new HashMap<String, String>();
        attrs.put(KeyCloakStaticConfig.STATUS,Project.StatusEnum.ACTIVE.toString());

        projectService.updateProjectInfo(projectName,attrs);

        res = new ResponseEntity<Void>(HttpStatus.OK);

        return res;

    }

    public ResponseEntity<Void> userIdProjectsProjectNameArchivePut(@ApiParam(value = "user id (not name or email)",required=true) @PathVariable("user_id") String userId
,@ApiParam(value = "project name",required=true) @PathVariable("project_name") String projectName) throws Exception{
        this.checkResource(userId,projectName);
        ResponseEntity<Void> res = null;
        Map<String,String> attrs = new HashMap<String, String>();

        attrs.put(KeyCloakStaticConfig.STATUS,Project.StatusEnum.ARCHIVE.toString());

        projectService.updateProjectInfo(projectName,attrs);

        res = new ResponseEntity<Void>(HttpStatus.OK);

        return res;
    }

    public ResponseEntity<Void> userIdProjectsProjectNameDelete(@ApiParam(value = "user id (not name or email)",required=true) @PathVariable("user_id") String userId
,@ApiParam(value = "project name",required=true) @PathVariable("project_name") String projectName) throws Exception{

        ResponseEntity<Void> res = null;

        this.checkResource(userId,projectName);

        if(!projectService.isOwnerOfProject(userId,projectName)){
            throw new APIException(userId+"is not the owner of project",HttpStatus.BAD_REQUEST);
        }
        projectService.deleteProjectById(projectName);

        res = new ResponseEntity<Void>(HttpStatus.OK);

        return res;
    }

    public ResponseEntity<Project> userIdProjectsProjectNameGet(@ApiParam(value = "user id (not name or email)",required=true) @PathVariable("user_id") String userId
,@ApiParam(value = "project name",required=true) @PathVariable("project_name") String projectName) throws Exception{
        ResponseEntity<Project> res = null;
        this.checkResource(userId,projectName);

        Boolean existsUserInProject = projectService.existsUserInProject(userId,projectName);

        if(!existsUserInProject){
            throw new APIException("Does Not Exists User In Project",HttpStatus.BAD_REQUEST);
        }

        Project project = projectService.getProjectByProjectName(projectName);
        res = new ResponseEntity<Project>(project,HttpStatus.OK);
        return res;
    }

    public ResponseEntity<List<Group>> userIdProjectsProjectNameGroupsGet(@ApiParam(value = "user id (not name or email)",required=true) @PathVariable("user_id") String userId
,@ApiParam(value = "project name",required=true) @PathVariable("project_name") String projectName
) throws Exception{
        ResponseEntity<List<Group>> res = null;

        this.checkResource(userId,projectName);

        List<Group> groups = projectService.getGroupsByProjectName(projectName);

        res = new ResponseEntity<List<Group>>(groups,HttpStatus.OK);
        return res;
    }

    public ResponseEntity<List<Member>> userIdProjectsProjectNameGroupsGroupNameMembersGet(@ApiParam(value = "user id (not name or email)",required=true) @PathVariable("user_id") String userId
,@ApiParam(value = "project name",required=true) @PathVariable("project_name") String projectName
,@ApiParam(value = "group_name",required=true) @PathVariable("group_name") String groupName
) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<List<Member>>(objectMapper.readValue("[ {\n  \"name\" : \"name\",\n  \"id\" : \"id\",\n  \"email\" : \"email\"\n}, {\n  \"name\" : \"name\",\n  \"id\" : \"id\",\n  \"email\" : \"email\"\n} ]", List.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<Member>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<Member>>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Void> userIdProjectsProjectNameGroupsGroupNamePatch(@ApiParam(value = "user id (not name or email)",required=true) @PathVariable("user_id") String userId
,@ApiParam(value = "project name",required=true) @PathVariable("project_name") String projectName
,@ApiParam(value = "group_name",required=true) @PathVariable("group_name") String groupName
,@NotNull @ApiParam(value = "member id", required = true) @Valid @RequestParam(value = "member_id", required = true) String memberId
) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Void> userIdProjectsProjectNameGroupsGroupNameInvitationPut(@ApiParam(value = "user id (not name or email)",required=true) @PathVariable("user_id") String userId
,@ApiParam(value = "project name",required=true) @PathVariable("project_name") String projectName
,@ApiParam(value = "group_name",required=true) @PathVariable("group_name") String groupName
,@NotNull @ApiParam(value = "email", required = true) @Valid @RequestParam(value = "email", required = true) String email
) throws APIException, KeyCloakAdminException {
        ResponseEntity<Void> res = null;

        this.checkResource(userId,projectName);
        projectService.inviteUserToGroup(email,projectName,groupName);

        return res;
    }

    public ResponseEntity<Void> userIdProjectsProjectNameMembersMemberIdDelete(@ApiParam(value = "user id (not name or email)",required=true) @PathVariable("user_id") String userId
,@ApiParam(value = "project name",required=true) @PathVariable("project_name") String projectName
,@ApiParam(value = "user id == member_id",required=true) @PathVariable("member_id") String memberId
) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Void> userIdProjectsProjectNamePatch(@ApiParam(value = "" ,required=true )  @Valid @RequestBody UpdateProject body
,@ApiParam(value = "user id (not name or email)",required=true) @PathVariable("user_id") String userId
,@ApiParam(value = "project name",required=true) @PathVariable("project_name") String projectName
) throws Exception{
        ResponseEntity<Void> res = null;
        Map<String,String> attrs = null;
        String owner = body.getOwner();
        String description = body.getDescription();

        if(!projectService.isOwnerOfProject(userId,projectName)){
            throw new APIException(userId+"is not the owner of project",HttpStatus.BAD_REQUEST);
        }

        if(!owner.isEmpty() || !description.isEmpty()){
            attrs = new HashMap<String, String>();
            if(!owner.isEmpty()){
                attrs.put(KeyCloakStaticConfig.OWNER,owner);
            }
            if(!description.isEmpty()){
                attrs.put(KeyCloakStaticConfig.DESCRIPTION,description);
            }
        }

        projectService.updateProjectInfo(projectName,attrs);

        res = new ResponseEntity<Void>(HttpStatus.OK);

        return res;
    }

    private void checkResource(String userId,String projectName) throws APIException {
        projectService.checkUserById(userId);
        projectService.checkProjectByProjectName(projectName);
    }

}
