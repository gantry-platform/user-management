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
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-03-30T15:23:45.786+09:00[Asia/Seoul]")
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

    public ResponseEntity<Void> projectsProjectIdActivePut(@ApiParam(value = "project id",required=true) @PathVariable("project_id") String projectId
) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Void> projectsProjectIdArchivePut(@ApiParam(value = "project id",required=true) @PathVariable("project_id") String projectId
) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Void> projectsProjectIdDelete(@ApiParam(value = "project id",required=true) @PathVariable("project_id") String projectId
) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Project> projectsProjectIdGet(@ApiParam(value = "project id",required=true) @PathVariable("project_id") String projectId
) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<Project>(objectMapper.readValue("{\n  \"owner\" : \"owner\",\n  \"pending_users\" : [ {\n    \"email\" : \"email\"\n  }, {\n    \"email\" : \"email\"\n  } ],\n  \"name\" : \"name\",\n  \"description\" : \"description\",\n  \"groups\" : [ {\n    \"members\" : [ {\n      \"email_verified\" : true,\n      \"user_id\" : \"user_id\",\n      \"user_name\" : \"user_name\",\n      \"email\" : \"email\"\n    }, {\n      \"email_verified\" : true,\n      \"user_id\" : \"user_id\",\n      \"user_name\" : \"user_name\",\n      \"email\" : \"email\"\n    } ],\n    \"name\" : \"name\",\n    \"id\" : \"id\",\n    \"display_name\" : \"display_name\"\n  }, {\n    \"members\" : [ {\n      \"email_verified\" : true,\n      \"user_id\" : \"user_id\",\n      \"user_name\" : \"user_name\",\n      \"email\" : \"email\"\n    }, {\n      \"email_verified\" : true,\n      \"user_id\" : \"user_id\",\n      \"user_name\" : \"user_name\",\n      \"email\" : \"email\"\n    } ],\n    \"name\" : \"name\",\n    \"id\" : \"id\",\n    \"display_name\" : \"display_name\"\n  } ],\n  \"id\" : \"id\",\n  \"display_name\" : \"display_name\",\n  \"status\" : \"archive\"\n}", Project.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<Project>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<Project>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<List<Group>> projectsProjectIdGroupsGet(@ApiParam(value = "project id",required=true) @PathVariable("project_id") String projectId
) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<List<Group>>(objectMapper.readValue("[ {\n  \"members\" : [ {\n    \"email_verified\" : true,\n    \"user_id\" : \"user_id\",\n    \"user_name\" : \"user_name\",\n    \"email\" : \"email\"\n  }, {\n    \"email_verified\" : true,\n    \"user_id\" : \"user_id\",\n    \"user_name\" : \"user_name\",\n    \"email\" : \"email\"\n  } ],\n  \"name\" : \"name\",\n  \"id\" : \"id\",\n  \"display_name\" : \"display_name\"\n}, {\n  \"members\" : [ {\n    \"email_verified\" : true,\n    \"user_id\" : \"user_id\",\n    \"user_name\" : \"user_name\",\n    \"email\" : \"email\"\n  }, {\n    \"email_verified\" : true,\n    \"user_id\" : \"user_id\",\n    \"user_name\" : \"user_name\",\n    \"email\" : \"email\"\n  } ],\n  \"name\" : \"name\",\n  \"id\" : \"id\",\n  \"display_name\" : \"display_name\"\n} ]", List.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<Group>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<Group>>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Void> projectsProjectIdGroupsGroupIdInvitationPut(@ApiParam(value = "project id",required=true) @PathVariable("project_id") String projectId
,@ApiParam(value = "group_id",required=true) @PathVariable("group_id") String groupId
,@NotNull @ApiParam(value = "email", required = true) @Valid @RequestParam(value = "email", required = true) String email
) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<List<Member>> projectsProjectIdGroupsGroupIdMembersGet(@ApiParam(value = "project id",required=true) @PathVariable("project_id") String projectId
,@ApiParam(value = "group id",required=true) @PathVariable("group_id") String groupId
) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<List<Member>>(objectMapper.readValue("[ {\n  \"email_verified\" : true,\n  \"user_id\" : \"user_id\",\n  \"user_name\" : \"user_name\",\n  \"email\" : \"email\"\n}, {\n  \"email_verified\" : true,\n  \"user_id\" : \"user_id\",\n  \"user_name\" : \"user_name\",\n  \"email\" : \"email\"\n} ]", List.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<Member>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<Member>>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Void> projectsProjectIdGroupsGroupIdMembersPatch(@ApiParam(value = "project id",required=true) @PathVariable("project_id") String projectId
,@ApiParam(value = "group id",required=true) @PathVariable("group_id") String groupId
,@NotNull @ApiParam(value = "member id", required = true) @Valid @RequestParam(value = "member_id", required = true) String memberId
) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Void> projectsProjectIdGroupsInvitationDelete(@ApiParam(value = "project id",required=true) @PathVariable("project_id") String projectId
,@NotNull @ApiParam(value = "", required = true) @Valid @RequestParam(value = "email", required = true) String email
) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Void> projectsProjectIdMembersMemberIdDelete(@ApiParam(value = "project id",required=true) @PathVariable("project_id") String projectId
,@ApiParam(value = "user id == member_id",required=true) @PathVariable("member_id") String memberId
) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Void> projectsProjectIdPatch(@ApiParam(value = "" ,required=true )  @Valid @RequestBody UpdateProject body
,@ApiParam(value = "project id",required=true) @PathVariable("project_id") String projectId
) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }

}
