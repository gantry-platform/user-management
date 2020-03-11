package kr.co.inslab.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiParam;
import kr.co.inslab.exception.APIException;
import kr.co.inslab.keycloak.KeyCloakStaticConfig;
import kr.co.inslab.model.UserInvitation;
import kr.co.inslab.service.UserService;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

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


    public ResponseEntity<String> usersInvitationPost(@ApiParam(value = "" ,required=true )  @Valid @RequestBody UserInvitation body) throws Exception {

        String email = body.getEamil();

        List<UserRepresentation> userRepresentations = this.userService.getUserByEmail(email);

        if(userRepresentations.size()!=0){
            throw new APIException(email,HttpStatus.CONFLICT);
        }

        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setEnabled(true);
        userRepresentation.setEmailVerified(true);
        userRepresentation.setEmail(email);
        userRepresentation.setUsername(email);

        List<String> actions = new ArrayList<String>();
        actions.add(KeyCloakStaticConfig.UPDATE_PROFILE);
        actions.add(KeyCloakStaticConfig.UPDATE_PASSWORD);
        actions.add(KeyCloakStaticConfig.VERIFY_EMAIL);
        userRepresentation.setRequiredActions(actions);


        this.userService.inviteUser(userRepresentation);

        List<UserRepresentation> invitedUser = this.userService.getUserByEmail(email);
        if (invitedUser.size() == 0){
            throw new APIException(email,HttpStatus.NOT_FOUND);
        }else if(invitedUser.size() > 1){
            throw new APIException(email,HttpStatus.CONFLICT);
        }

        //send email
        UserResource resource= this.userService.getUserResourceById(invitedUser.get(0).getId());
        resource.sendVerifyEmail();

        return new ResponseEntity<String>("Created",HttpStatus.CREATED);
    }

    public ResponseEntity<String> usersIdDelete(@ApiParam(value = "",required=true) @PathVariable("id") String id) throws Exception{

        UserResource userResource = this.userService.getUserResourceById(id);

        try {
            //TODO: javax.ws.rs 상위 예외 처리로 변경 필요
            UserRepresentation userRepresentation = userResource.toRepresentation();
        }catch (javax.ws.rs.NotFoundException e){
            throw new APIException(id,HttpStatus.NOT_FOUND);
        }

        this.userService.getUserResourceById(id).remove();

        return new ResponseEntity<String>("Success",HttpStatus.OK);
    }

}
