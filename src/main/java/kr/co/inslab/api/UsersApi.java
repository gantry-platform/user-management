/**
 * NOTE: This class is auto generated by the swagger code generator program (3.0.16).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package kr.co.inslab.api;

import kr.co.inslab.exception.ApiException;
import kr.co.inslab.exception.KeyCloakAdminException;
import kr.co.inslab.model.Error;
import kr.co.inslab.model.NewProject;
import kr.co.inslab.model.Project;
import kr.co.inslab.model.User;
import io.swagger.annotations.*;
import org.keycloak.common.VerificationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-03-27T14:04:16.475+09:00[Asia/Seoul]")
@Api(value = "Users", description = "the Users API")
public interface UsersApi {

    @ApiOperation(value = "유저 정보 조회", nickname = "usersGet", notes = "", response = User.class, tags={ "users", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = User.class),
            @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
            @ApiResponse(code = 404, message = "Not Found", response = Error.class) })
    @RequestMapping(value = "/users",
            produces = { "application/json" },
            method = RequestMethod.GET)
    ResponseEntity<User> usersGet(@ApiParam(value = "프로젝트 정보까지 포함") @Valid @RequestParam(value = "include_project", required = false) Boolean includeProject
    ) throws VerificationException;


    @ApiOperation(value = "신규 프로젝트 생성", nickname = "usersProjectsPost", notes = "", response = Project.class, tags={ "users", })
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Create", response = Project.class),
            @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
            @ApiResponse(code = 404, message = "Not Found", response = Error.class),
            @ApiResponse(code = 409, message = "Conflict", response = Error.class) })
    @RequestMapping(value = "/users/projects",
            produces = { "application/json" },
            consumes = { "application/json" },
            method = RequestMethod.POST)
    ResponseEntity<Project> usersProjectsPost(@ApiParam(value = "" ,required=true )  @Valid @RequestBody NewProject body
    ) throws ApiException, KeyCloakAdminException, VerificationException;

}
