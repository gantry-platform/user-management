package kr.co.inslab.api;

import kr.co.inslab.model.Error;
import kr.co.inslab.model.Group;
import kr.co.inslab.model.Member;
import kr.co.inslab.model.Project;
import kr.co.inslab.model.UpdateProject;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-03-16T10:39:54.886+09:00[Asia/Seoul]")
@Controller
public class ProjectsApiController implements ProjectsApi {

    private static final Logger log = LoggerFactory.getLogger(ProjectsApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public ProjectsApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<Void> userIdProjectsProjectNameActivePut(@ApiParam(value = "user id (not name or email)",required=true) @PathVariable("user_id") String userId
,@ApiParam(value = "project name",required=true) @PathVariable("project_name") String projectName
) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Void> userIdProjectsProjectNameArchivePut(@ApiParam(value = "user id (not name or email)",required=true) @PathVariable("user_id") String userId
,@ApiParam(value = "project name",required=true) @PathVariable("project_name") String projectName
) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Void> userIdProjectsProjectNameDelete(@ApiParam(value = "user id (not name or email)",required=true) @PathVariable("user_id") String userId
,@ApiParam(value = "project name",required=true) @PathVariable("project_name") String projectName
) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Project> userIdProjectsProjectNameGet(@ApiParam(value = "user id (not name or email)",required=true) @PathVariable("user_id") String userId
,@ApiParam(value = "project name",required=true) @PathVariable("project_name") String projectName
) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<Project>(objectMapper.readValue("{\n  \"owner\" : \"owner\",\n  \"name\" : \"name\",\n  \"description\" : \"description\",\n  \"groups\" : [ {\n    \"members\" : [ {\n      \"name\" : \"name\",\n      \"id\" : \"id\",\n      \"email\" : \"email\"\n    }, {\n      \"name\" : \"name\",\n      \"id\" : \"id\",\n      \"email\" : \"email\"\n    } ],\n    \"name\" : \"name\",\n    \"display_name\" : \"display_name\"\n  }, {\n    \"members\" : [ {\n      \"name\" : \"name\",\n      \"id\" : \"id\",\n      \"email\" : \"email\"\n    }, {\n      \"name\" : \"name\",\n      \"id\" : \"id\",\n      \"email\" : \"email\"\n    } ],\n    \"name\" : \"name\",\n    \"display_name\" : \"display_name\"\n  } ],\n  \"display_name\" : \"display_name\",\n  \"status\" : \"archive\"\n}", Project.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<Project>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<Project>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<List<Group>> userIdProjectsProjectNameGroupsGet(@ApiParam(value = "user id (not name or email)",required=true) @PathVariable("user_id") String userId
,@ApiParam(value = "project name",required=true) @PathVariable("project_name") String projectName
) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<List<Group>>(objectMapper.readValue("[ {\n  \"members\" : [ {\n    \"name\" : \"name\",\n    \"id\" : \"id\",\n    \"email\" : \"email\"\n  }, {\n    \"name\" : \"name\",\n    \"id\" : \"id\",\n    \"email\" : \"email\"\n  } ],\n  \"name\" : \"name\",\n  \"display_name\" : \"display_name\"\n}, {\n  \"members\" : [ {\n    \"name\" : \"name\",\n    \"id\" : \"id\",\n    \"email\" : \"email\"\n  }, {\n    \"name\" : \"name\",\n    \"id\" : \"id\",\n    \"email\" : \"email\"\n  } ],\n  \"name\" : \"name\",\n  \"display_name\" : \"display_name\"\n} ]", List.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<Group>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<Group>>(HttpStatus.NOT_IMPLEMENTED);
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

    public ResponseEntity<Void> userIdProjectsProjectNameGroupsGroupNamePut(@ApiParam(value = "user id (not name or email)",required=true) @PathVariable("user_id") String userId
,@ApiParam(value = "project name",required=true) @PathVariable("project_name") String projectName
,@ApiParam(value = "group_name",required=true) @PathVariable("group_name") String groupName
,@NotNull @ApiParam(value = "email", required = true) @Valid @RequestParam(value = "email", required = true) String email
) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
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
) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }

}
