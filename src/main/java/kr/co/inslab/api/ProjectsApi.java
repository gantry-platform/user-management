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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-03-27T14:04:16.475+09:00[Asia/Seoul]")
@Api(value = "Projects", description = "the Projects API")
public interface ProjectsApi {

    @ApiOperation(value = "프로젝트 활성화", nickname = "projectsProjectIdActivePut", notes = "", tags={ "projects", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
            @ApiResponse(code = 404, message = "Not Found", response = Error.class) })
    @RequestMapping(value = "/projects/{project_id}/active",
            produces = { "application/json" },
            method = RequestMethod.PUT)
    ResponseEntity<Void> projectsProjectIdActivePut(@ApiParam(value = "project id",required=true) @PathVariable("project_id") String projectId
    ) throws Exception;


    @ApiOperation(value = "프로젝트 보관", nickname = "projectsProjectIdArchivePut", notes = "", tags={ "projects", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
            @ApiResponse(code = 404, message = "Not Found", response = Error.class) })
    @RequestMapping(value = "/projects/{project_id}/archive",
            produces = { "application/json" },
            method = RequestMethod.PUT)
    ResponseEntity<Void> projectsProjectIdArchivePut(@ApiParam(value = "project id",required=true) @PathVariable("project_id") String projectId
    ) throws Exception;


    @ApiOperation(value = "프로젝트 삭제", nickname = "projectsProjectIdDelete", notes = "", tags={ "projects", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
            @ApiResponse(code = 404, message = "Not Found", response = Error.class) })
    @RequestMapping(value = "/projects/{project_id}",
            produces = { "application/json" },
            method = RequestMethod.DELETE)
    ResponseEntity<Void> projectsProjectIdDelete(@ApiParam(value = "project id",required=true) @PathVariable("project_id") String projectId
    ) throws  Exception;


    @ApiOperation(value = "프로젝트 정보 조회", nickname = "projectsProjectIdGet", notes = "", response = Project.class, tags={ "projects", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Project.class),
            @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
            @ApiResponse(code = 404, message = "Not Found", response = Error.class) })
    @RequestMapping(value = "/projects/{project_id}",
            produces = { "application/json" },
            method = RequestMethod.GET)
    ResponseEntity<Project> projectsProjectIdGet(@ApiParam(value = "project id",required=true) @PathVariable("project_id") String projectId
    ) throws Exception;


    @ApiOperation(value = "프로젝트의 전체 그룹정도(dev,ops,admin) 조회", nickname = "projectsProjectIdGroupsGet", notes = "", response = Group.class, responseContainer = "List", tags={ "projects", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Group.class, responseContainer = "List"),
            @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
            @ApiResponse(code = 404, message = "Not Found", response = Error.class) })
    @RequestMapping(value = "/projects/{project_id}/groups",
            produces = { "application/json" },
            method = RequestMethod.GET)
    ResponseEntity<List<Group>> projectsProjectIdGroupsGet(@ApiParam(value = "project id",required=true) @PathVariable("project_id") String projectId
    ) throws Exception;


    @ApiOperation(value = "특정 그룹으로 맴버초대", nickname = "projectsProjectIdGroupsGroupIdInvitationPut", notes = "", tags={ "projects", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
            @ApiResponse(code = 404, message = "Not Found", response = Error.class),
            @ApiResponse(code = 409, message = "Conflict", response = Error.class) })
    @RequestMapping(value = "/projects/{project_id}/groups/{group_id}/invitation",
            produces = { "application/json" },
            method = RequestMethod.PUT)
    ResponseEntity<Void> projectsProjectIdGroupsGroupIdInvitationPut(@ApiParam(value = "project id",required=true) @PathVariable("project_id") String projectId
            ,@ApiParam(value = "group_id",required=true) @PathVariable("group_id") String groupId
            ,@NotNull @ApiParam(value = "email", required = true) @Valid @RequestParam(value = "email", required = true) String email
    ) throws Exception;


    @ApiOperation(value = "특정 프로젝트의 전체 맴버를 조회한다.", nickname = "projectsProjectIdGroupsGroupIdMembersGet", notes = "", response = Member.class, responseContainer = "List", tags={ "projects", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Member.class, responseContainer = "List"),
            @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
            @ApiResponse(code = 409, message = "Conflict", response = Error.class) })
    @RequestMapping(value = "/projects/{project_id}/groups/{group_id}/members",
            produces = { "application/json" },
            method = RequestMethod.GET)
    ResponseEntity<List<Member>> projectsProjectIdGroupsGroupIdMembersGet(@ApiParam(value = "project id",required=true) @PathVariable("project_id") String projectId
            ,@ApiParam(value = "group id",required=true) @PathVariable("group_id") String groupId
    ) throws Exception;


    @ApiOperation(value = "맴버의 그룹정보를 변경한다.(e.g.) admin to ops)", nickname = "projectsProjectIdGroupsGroupIdMembersPatch", notes = "", tags={ "projects", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
            @ApiResponse(code = 404, message = "Not Found", response = Error.class) })
    @RequestMapping(value = "/projects/{project_id}/groups/{group_id}/members",
            produces = { "application/json" },
            method = RequestMethod.PATCH)
    ResponseEntity<Void> projectsProjectIdGroupsGroupIdMembersPatch(@ApiParam(value = "project id",required=true) @PathVariable("project_id") String projectId
            ,@ApiParam(value = "group id",required=true) @PathVariable("group_id") String groupId
            ,@NotNull @ApiParam(value = "member id", required = true) @Valid @RequestParam(value = "member_id", required = true) String memberId
    ) throws Exception;


    @ApiOperation(value = "특정 맴버를 프로젝트에서 삭제한다.(그룹이 아니라 프로젝트임)", nickname = "projectsProjectIdMembersMemberIdDelete", notes = "", tags={ "projects", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
            @ApiResponse(code = 404, message = "Not Found", response = Error.class) })
    @RequestMapping(value = "/projects/{project_id}/members/{member_id}",
            produces = { "application/json" },
            method = RequestMethod.DELETE)
    ResponseEntity<Void> projectsProjectIdMembersMemberIdDelete(@ApiParam(value = "project id",required=true) @PathVariable("project_id") String projectId
            ,@ApiParam(value = "user id == member_id",required=true) @PathVariable("member_id") String memberId
    ) throws Exception;


    @ApiOperation(value = "프로젝트 업데이트 (owner and description)", nickname = "projectsProjectIdPatch", notes = "", tags={ "projects", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
            @ApiResponse(code = 404, message = "Not Found", response = Error.class) })
    @RequestMapping(value = "/projects/{project_id}",
            produces = { "application/json" },
            consumes = { "application/json" },
            method = RequestMethod.PATCH)
    ResponseEntity<Void> projectsProjectIdPatch(@ApiParam(value = "" ,required=true )  @Valid @RequestBody UpdateProject body
            ,@ApiParam(value = "project id",required=true) @PathVariable("project_id") String projectId
    ) throws Exception;


    @RequestMapping(value = "/projects/confirm-join",method = RequestMethod.GET)
    public String confirmJoin(WebRequest request, @RequestParam("token") String token) throws Exception;


}
