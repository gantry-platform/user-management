package kr.co.inslab.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.inslab.model.NewProject;
import kr.co.inslab.model.Project;
import kr.co.inslab.model.User;
import kr.co.inslab.gantry.GantryProject;
import kr.co.inslab.gantry.GantryUser;
import org.keycloak.TokenVerifier;
import org.keycloak.representations.AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-02-26T15:17:27.527+09:00[Asia/Seoul]")
@Controller
public class UsersApiController implements UsersApi {

    private static final Logger logger = LoggerFactory.getLogger(UsersApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    private final GantryUser gantryUser;

    private final GantryProject gantryProject;

    @org.springframework.beans.factory.annotation.Autowired
    public UsersApiController(ObjectMapper objectMapper, HttpServletRequest request, GantryUser gantryUser, GantryProject gantryProject) {
        this.objectMapper = objectMapper;
        this.request = request;
        this.gantryUser = gantryUser;
        this.gantryProject = gantryProject;
    }


    @Override
    public ResponseEntity<User> usersGet(@Valid Boolean includeProject) throws Exception {

        //임시코드
        String userId = this.getUserId(request);

        gantryUser.checkUserById(userId);

        if(includeProject==null){
            includeProject=false;
        }


        User user = gantryUser.getUserInfoById(userId,includeProject);
        ResponseEntity<User> res = new ResponseEntity<User>(user,HttpStatus.OK);

        return res;
    }

    //임시코드
    private final String getUserId(HttpServletRequest request) throws Exception {
        String userId = null;
        String token = request.getHeader("Authorization");
        if (token != null && !token.isEmpty()) {
            String[] splitToken = token.split(" ");
            System.out.println(splitToken[1]);
            AccessToken accessToken = TokenVerifier.create(splitToken[1], AccessToken.class).getToken();
            userId = accessToken.getSubject();
            logger.debug("subject:"+userId);
        }
        if(userId==null){
            throw new ApiException("Invaild userId", HttpStatus.BAD_REQUEST);
        }
        return userId;
    }
}
