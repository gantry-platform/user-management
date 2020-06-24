package kr.co.inslab.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiParam;
import kr.co.inslab.auth.OAuthToken;
import kr.co.inslab.model.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Profile("test")
@Controller
public class TokenApiController implements TokenApi{

    private static final Logger log = LoggerFactory.getLogger(TokenApiController.class);

    private final OAuthToken oAuthToken;

    public TokenApiController(ObjectMapper objectMapper, HttpServletRequest request, OAuthToken oAuthToken) {
        this.oAuthToken = oAuthToken;
    }

    public ResponseEntity<Token> tokenPost(@ApiParam(value = "", required=true) @RequestParam(value="username", required=true)  String username
            , @ApiParam(value = "", required=true) @RequestParam(value="password", required=true)  String password
    ) throws Exception{
        Token token = this.oAuthToken.getToken(username,password);
        return new ResponseEntity<Token>(token, HttpStatus.OK);
    }
}
