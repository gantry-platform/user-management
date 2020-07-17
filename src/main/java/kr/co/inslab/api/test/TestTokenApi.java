package kr.co.inslab.api.test;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.inslab.model.Token;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

public interface TestTokenApi {

    @ApiOperation(value = "password grant 토큰 획득", nickname = "tokenPost", notes = "Call keycloak token endpoint", response = Token.class, tags={ "token", })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = Token.class) })
    @RequestMapping(value = "/token",
            produces = { "application/json" },
            consumes = { "application/x-www-form-urlencoded" },
            method = RequestMethod.POST)
    ResponseEntity<Token> tokenPost(@ApiParam(value = "", required=true) @RequestParam(value="username", required=true)  String username
            , @ApiParam(value = "", required=true) @RequestParam(value="password", required=true)  String password
    )throws Exception;;
}
