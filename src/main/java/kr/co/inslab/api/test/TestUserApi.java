package kr.co.inslab.api.test;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.inslab.model.Error;
import kr.co.inslab.model.NewUser;
import kr.co.inslab.model.UpdateUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

public interface TestUserApi {

    @ApiOperation(value = "사용자 생성", nickname = "usersPost", notes = "", tags={ "users", })
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = Error.class)})
    @RequestMapping(value = "/test/users",
            produces = { "application/json" },
            consumes = { "application/json" },
            method = RequestMethod.POST)
    ResponseEntity<Void> usersPost(  @Valid @RequestBody NewUser body) throws Exception;

    @ApiOperation(value = "사용자 정보 삭제", nickname = "usersUserIdDelete", notes = "", tags={ "users", })
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No Content"),
            @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
            @ApiResponse(code = 404, message = "Not Found", response = Error.class) })
    @RequestMapping(value = "/test/users/{user_id}",
            produces = { "application/json" },
            method = RequestMethod.DELETE)
    ResponseEntity<Void> usersUserIdDelete(@ApiParam(value = "user_id",required=true) @PathVariable("user_id") String userId
    ) throws Exception;
}
