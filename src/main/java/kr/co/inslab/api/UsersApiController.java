package kr.co.inslab.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiParam;
import kr.co.inslab.gantry.GantryUser;
import kr.co.inslab.gantry.UserException;
import kr.co.inslab.model.UpdateUser;
import kr.co.inslab.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-07-16T16:56:51.496+09:00[Asia/Seoul]")
@Controller
public class UsersApiController implements UsersApi {

    private static final Logger log = LoggerFactory.getLogger(UsersApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    private final GantryUser gantryUser;

    @org.springframework.beans.factory.annotation.Autowired
    public UsersApiController(ObjectMapper objectMapper, HttpServletRequest request, GantryUser gantryUser) {
        this.objectMapper = objectMapper;
        this.request = request;
        this.gantryUser = gantryUser;
    }

    public ResponseEntity<Void> usersUserIdDelete(@ApiParam(value = "user_id",required=true) @PathVariable("user_id") String userId
) throws Exception {
        //임시코드
        this.gantryUser.checkUserById(userId);

        this.gantryUser.disableUser(userId);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    public ResponseEntity<User> usersUserIdGet(@ApiParam(value = "유저정보조회",required=true) @PathVariable("user_id") String userId
) throws Exception {
        //임시코드
        this.gantryUser.checkUserById(userId);

        User user = this.gantryUser.getUserInfoById(userId);
        return new ResponseEntity<User>(user,HttpStatus.OK);

    }

    public ResponseEntity<Void> usersUserIdPut(@ApiParam(value = "" ,required=true )  @Valid @RequestBody UpdateUser body
,@ApiParam(value = "user_id",required=true) @PathVariable("user_id") String userId
) throws Exception {
        //임시코드
        this.gantryUser.checkUserById(userId);

        this.gantryUser.updateUser(userId,body);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);

    }

}
