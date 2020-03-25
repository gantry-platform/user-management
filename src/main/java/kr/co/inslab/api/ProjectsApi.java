/**
 * NOTE: This class is auto generated by the swagger code generator program (3.0.16).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package kr.co.inslab.api;

import io.swagger.annotations.*;
import kr.co.inslab.model.Error;
import kr.co.inslab.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-03-20T16:37:02.439+09:00[Asia/Seoul]")
@Api(value = "Projects", description = "the Projects API")
public interface ProjectsApi {

    @ApiOperation(value = "프로젝트 활성화", nickname = "userIdProjectsProjectIdActivePut", notes = "", tags={ "projects", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Success"),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class) })
    @RequestMapping(value = "/{user_id}/projects/{project_id}/active",
        produces = { "application/json" }, 
        method = RequestMethod.PUT)
    ResponseEntity<Void> userIdProjectsProjectIdActivePut(@ApiParam(value = "user id (not name or email)",required=true) @PathVariable("user_id") String userId
,@ApiParam(value = "project id",required=true) @PathVariable("project_id") String projectId
) throws Exception;


    @ApiOperation(value = "프로젝트 보관", nickname = "userIdProjectsProjectIdArchivePut", notes = "", tags={ "projects", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Success"),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class) })
    @RequestMapping(value = "/{user_id}/projects/{project_id}/archive",
        produces = { "application/json" }, 
        method = RequestMethod.PUT)
    ResponseEntity<Void> userIdProjectsProjectIdArchivePut(@ApiParam(value = "user id (not name or email)",required=true) @PathVariable("user_id") String userId
,@ApiParam(value = "project id",required=true) @PathVariable("project_id") String projectId
)throws Exception;


    @ApiOperation(value = "프로젝트 삭제", nickname = "userIdProjectsProjectIdDelete", notes = "", tags={ "projects", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Success"),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class) })
    @RequestMapping(value = "/{user_id}/projects/{project_id}",
        produces = { "application/json" }, 
        method = RequestMethod.DELETE)
    ResponseEntity<Void> userIdProjectsProjectIdDelete(@ApiParam(value = "user id (not name or email)",required=true) @PathVariable("user_id") String userId
,@ApiParam(value = "project id",required=true) @PathVariable("project_id") String projectId
)throws Exception;


    @ApiOperation(value = "프로젝트 정보 조회", nickname = "userIdProjectsProjectIdGet", notes = "", response = Project.class, tags={ "projects", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Success", response = Project.class),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class) })
    @RequestMapping(value = "/{user_id}/projects/{project_id}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<Project> userIdProjectsProjectIdGet(@ApiParam(value = "user id (not name or email)",required=true) @PathVariable("user_id") String userId
,@ApiParam(value = "project id",required=true) @PathVariable("project_id") String projectId
)throws Exception;


    @ApiOperation(value = "프로젝트의 전체 그룹정도(dev,ops,admin) 조회", nickname = "userIdProjectsProjectIdGroupsGet", notes = "", response = Group.class, responseContainer = "List", tags={ "projects", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Success", response = Group.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class) })
    @RequestMapping(value = "/{user_id}/projects/{project_id}/groups",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<Group>> userIdProjectsProjectIdGroupsGet(@ApiParam(value = "user id (not name or email)",required=true) @PathVariable("user_id") String userId
,@ApiParam(value = "project id",required=true) @PathVariable("project_id") String projectId
)throws Exception;


    @ApiOperation(value = "특정 그룹으로 맴버초대", nickname = "userIdProjectsProjectIdGroupsGroupIdInvitationPut", notes = "", tags={ "projects", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Success"),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class),
        @ApiResponse(code = 409, message = "Conflict", response = Error.class) })
    @RequestMapping(value = "/{user_id}/projects/{project_id}/groups/{group_id}/invitation",
        produces = { "application/json" }, 
        method = RequestMethod.PUT)
    ResponseEntity<Void> userIdProjectsProjectIdGroupsGroupIdInvitationPut(@ApiParam(value = "user id (not name or email)",required=true) @PathVariable("user_id") String userId
,@ApiParam(value = "project id",required=true) @PathVariable("project_id") String projectId
,@ApiParam(value = "group_id",required=true) @PathVariable("group_id") String groupId
,@NotNull @ApiParam(value = "email", required = true) @Valid @RequestParam(value = "email", required = true) String email
)throws Exception;


    @ApiOperation(value = "특정 프로젝트의 전체 맴버를 조회한다.", nickname = "userIdProjectsProjectIdGroupsGroupIdMembersGet", notes = "", response = Member.class, responseContainer = "List", tags={ "projects", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Success", response = Member.class, responseContainer = "List"),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 409, message = "Conflict", response = Error.class) })
    @RequestMapping(value = "/{user_id}/projects/{project_id}/groups/{group_id}/members",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<Member>> userIdProjectsProjectIdGroupsGroupIdMembersGet(@ApiParam(value = "user id (not name or email)",required=true) @PathVariable("user_id") String userId
,@ApiParam(value = "project id",required=true) @PathVariable("project_id") String projectId
,@ApiParam(value = "group id",required=true) @PathVariable("group_id") String groupId
)throws Exception;


    @ApiOperation(value = "맴버의 그룹정보를 변경한다.(e.g.) admin to ops)", nickname = "userIdProjectsProjectIdGroupsGroupIdMembersPatch", notes = "", tags={ "projects", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Success"),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class) })
    @RequestMapping(value = "/{user_id}/projects/{project_id}/groups/{group_id}/members",
        produces = { "application/json" }, 
        method = RequestMethod.PATCH)
    ResponseEntity<Void> userIdProjectsProjectIdGroupsGroupIdMembersPatch(@ApiParam(value = "user id (not name or email)",required=true) @PathVariable("user_id") String userId
,@ApiParam(value = "project id",required=true) @PathVariable("project_id") String projectId
,@ApiParam(value = "group id",required=true) @PathVariable("group_id") String groupId
,@NotNull @ApiParam(value = "member id", required = true) @Valid @RequestParam(value = "member_id", required = true) String memberId
)throws Exception;


    @ApiOperation(value = "특정 맴버를 프로젝트에서 삭제한다.(그룹이 아니라 프로젝트임)", nickname = "userIdProjectsProjectIdMembersMemberIdDelete", notes = "", tags={ "projects", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Success"),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class) })
    @RequestMapping(value = "/{user_id}/projects/{project_id}/members/{member_id}",
        produces = { "application/json" }, 
        method = RequestMethod.DELETE)
    ResponseEntity<Void> userIdProjectsProjectIdMembersMemberIdDelete(@ApiParam(value = "user id (not name or email)",required=true) @PathVariable("user_id") String userId
,@ApiParam(value = "project id",required=true) @PathVariable("project_id") String projectId
,@ApiParam(value = "user id == member_id",required=true) @PathVariable("member_id") String memberId
)throws Exception;


    @ApiOperation(value = "프로젝트 업데이트 (owner and description)", nickname = "userIdProjectsProjectIdPatch", notes = "", tags={ "projects", })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Success"),
        @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
        @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
        @ApiResponse(code = 404, message = "Not Found", response = Error.class) })
    @RequestMapping(value = "/{user_id}/projects/{project_id}",
        produces = { "application/json" }, 
        consumes = { "application/json" },
        method = RequestMethod.PATCH)
    ResponseEntity<Void> userIdProjectsProjectIdPatch(@ApiParam(value = "" ,required=true )  @Valid @RequestBody UpdateProject body
,@ApiParam(value = "user id (not name or email)",required=true) @PathVariable("user_id") String userId
,@ApiParam(value = "project id",required=true) @PathVariable("project_id") String projectId
)throws Exception;

    @RequestMapping(value = "/projects/confirm-join",method = RequestMethod.GET)
    public String confirmJoin(WebRequest request,@RequestParam("token") String token);

}
