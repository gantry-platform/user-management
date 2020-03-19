/**
 * NOTE: This class is auto generated by the swagger code generator program (3.0.16).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package kr.co.inslab.api;

import kr.co.inslab.model.Error;
import kr.co.inslab.model.Group;
import kr.co.inslab.model.Member;
import kr.co.inslab.model.Project;
import kr.co.inslab.model.UpdateProject;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.CookieValue;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Map;
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-03-16T10:39:54.886+09:00[Asia/Seoul]")
@Api(value = "Projects", description = "the Projects API")
public interface ProjectsApi {

    @ApiOperation(value = "프로젝트 활성화", nickname = "userIdProjectsProjectNameActivePut", notes = "", tags={ "projects", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Success"),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class) })
    @RequestMapping(value = "/{user_id}/projects/{project_name}/active",
        produces = { "application/json" }, 
        method = RequestMethod.PUT)
    ResponseEntity<Void> userIdProjectsProjectNameActivePut(@ApiParam(value = "user id (not name or email)",required=true) @PathVariable("user_id") String userId
,@ApiParam(value = "project name",required=true) @PathVariable("project_name") String projectName
);


    @ApiOperation(value = "프로젝트 보관", nickname = "userIdProjectsProjectNameArchivePut", notes = "", tags={ "projects", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Success"),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class) })
    @RequestMapping(value = "/{user_id}/projects/{project_name}/archive",
        produces = { "application/json" }, 
        method = RequestMethod.PUT)
    ResponseEntity<Void> userIdProjectsProjectNameArchivePut(@ApiParam(value = "user id (not name or email)",required=true) @PathVariable("user_id") String userId
,@ApiParam(value = "project name",required=true) @PathVariable("project_name") String projectName
);


    @ApiOperation(value = "프로젝트 삭제", nickname = "userIdProjectsProjectNameDelete", notes = "", tags={ "projects", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Success"),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class) })
    @RequestMapping(value = "/{user_id}/projects/{project_name}",
        produces = { "application/json" }, 
        method = RequestMethod.DELETE)
    ResponseEntity<Void> userIdProjectsProjectNameDelete(@ApiParam(value = "user id (not name or email)",required=true) @PathVariable("user_id") String userId
,@ApiParam(value = "project name",required=true) @PathVariable("project_name") String projectName
);


    @ApiOperation(value = "프로젝트 정보 조회", nickname = "userIdProjectsProjectNameGet", notes = "", response = Project.class, tags={ "projects", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Success", response = Project.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class) })
    @RequestMapping(value = "/{user_id}/projects/{project_name}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<Project> userIdProjectsProjectNameGet(@ApiParam(value = "user id (not name or email)",required=true) @PathVariable("user_id") String userId
,@ApiParam(value = "project name",required=true) @PathVariable("project_name") String projectName
) throws Exception;


    @ApiOperation(value = "프로젝트의 전체 그룹정도(dev,ops,admin) 조회", nickname = "userIdProjectsProjectNameGroupsGet", notes = "", response = Group.class, responseContainer = "List", tags={ "projects", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Success", response = Group.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class) })
    @RequestMapping(value = "/{user_id}/projects/{project_name}/groups",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<Group>> userIdProjectsProjectNameGroupsGet(@ApiParam(value = "user id (not name or email)",required=true) @PathVariable("user_id") String userId
,@ApiParam(value = "project name",required=true) @PathVariable("project_name") String projectName
);


    @ApiOperation(value = "특정 프로젝트의 전체 맴버를 조회한다.", nickname = "userIdProjectsProjectNameGroupsGroupNameMembersGet", notes = "", response = Member.class, responseContainer = "List", tags={ "projects", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Success", response = Member.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 409, message = "Conflict", response = Error.class) })
    @RequestMapping(value = "/{user_id}/projects/{project_name}/groups/{group_name}/members",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<Member>> userIdProjectsProjectNameGroupsGroupNameMembersGet(@ApiParam(value = "user id (not name or email)",required=true) @PathVariable("user_id") String userId
,@ApiParam(value = "project name",required=true) @PathVariable("project_name") String projectName
,@ApiParam(value = "group_name",required=true) @PathVariable("group_name") String groupName
);


    @ApiOperation(value = "맴버의 그룹정보를 변경한다.(e.g.) admin to ops)", nickname = "userIdProjectsProjectNameGroupsGroupNamePatch", notes = "", tags={ "projects", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Success"),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class) })
    @RequestMapping(value = "/{user_id}/projects/{project_name}/groups/{group_name}",
        produces = { "application/json" }, 
        method = RequestMethod.PATCH)
    ResponseEntity<Void> userIdProjectsProjectNameGroupsGroupNamePatch(@ApiParam(value = "user id (not name or email)",required=true) @PathVariable("user_id") String userId
,@ApiParam(value = "project name",required=true) @PathVariable("project_name") String projectName
,@ApiParam(value = "group_name",required=true) @PathVariable("group_name") String groupName
,@NotNull @ApiParam(value = "member id", required = true) @Valid @RequestParam(value = "member_id", required = true) String memberId
);


    @ApiOperation(value = "특정 그룹으로 맴버초대", nickname = "userIdProjectsProjectNameGroupsGroupNamePut", notes = "", tags={ "projects", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Success"),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 409, message = "Conflict", response = Error.class) })
    @RequestMapping(value = "/{user_id}/projects/{project_name}/groups/{group_name}",
        produces = { "application/json" }, 
        method = RequestMethod.PUT)
    ResponseEntity<Void> userIdProjectsProjectNameGroupsGroupNamePut(@ApiParam(value = "user id (not name or email)",required=true) @PathVariable("user_id") String userId
,@ApiParam(value = "project name",required=true) @PathVariable("project_name") String projectName
,@ApiParam(value = "group_name",required=true) @PathVariable("group_name") String groupName
,@NotNull @ApiParam(value = "email", required = true) @Valid @RequestParam(value = "email", required = true) String email
);


    @ApiOperation(value = "특정 맴버를 프로젝트에서 삭제한다.(그룹이 아니라 프로젝트임)", nickname = "userIdProjectsProjectNameMembersMemberIdDelete", notes = "", tags={ "projects", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Success"),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class) })
    @RequestMapping(value = "/{user_id}/projects/{project_name}/members/{member_id}",
        produces = { "application/json" }, 
        method = RequestMethod.DELETE)
    ResponseEntity<Void> userIdProjectsProjectNameMembersMemberIdDelete(@ApiParam(value = "user id (not name or email)",required=true) @PathVariable("user_id") String userId
,@ApiParam(value = "project name",required=true) @PathVariable("project_name") String projectName
,@ApiParam(value = "user id == member_id",required=true) @PathVariable("member_id") String memberId
);


    @ApiOperation(value = "프로젝트 업데이트 (owner and description)", nickname = "userIdProjectsProjectNamePatch", notes = "", tags={ "projects", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Success"),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class) })
    @RequestMapping(value = "/{user_id}/projects/{project_name}",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.PATCH)
    ResponseEntity<Void> userIdProjectsProjectNamePatch(@ApiParam(value = "" ,required=true )  @Valid @RequestBody UpdateProject body
,@ApiParam(value = "user id (not name or email)",required=true) @PathVariable("user_id") String userId
,@ApiParam(value = "project name",required=true) @PathVariable("project_name") String projectName
);

}
