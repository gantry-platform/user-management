package kr.co.inslab.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.inslab.exception.APIException;
import kr.co.inslab.exception.KeyCloakAdminException;
import kr.co.inslab.model.NewProject;
import kr.co.inslab.model.Project;
import kr.co.inslab.model.User;
import kr.co.inslab.service.UserService;
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

    private final UserService userService;

    @org.springframework.beans.factory.annotation.Autowired
    public UsersApiController(ObjectMapper objectMapper, HttpServletRequest request, UserService userService) {
        this.objectMapper = objectMapper;
        this.request = request;
        this.userService = userService;
    }


    @Override
    public ResponseEntity<User> usersGet(String xUserId, @Valid Boolean includeProject) {
        if(includeProject==null){
            includeProject=false;
        }
        User user = userService.getUserInfoById(xUserId,includeProject);
        ResponseEntity<User> res = new ResponseEntity<User>(user,HttpStatus.OK);

        return res;
    }

    @Override
    public ResponseEntity<Project> usersProjectsPost(@Valid NewProject body, String xUserId) throws APIException, KeyCloakAdminException {
        userService.checkUserById(xUserId);

        String displayName = body.getDisplayName();
        String description = body.getDescription();

        Project project = userService.createProject(xUserId,displayName,description);

        ResponseEntity<Project> res = new ResponseEntity<Project>(project,HttpStatus.OK);

        return res;
    }
}
