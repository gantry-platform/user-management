package kr.co.inslab.api;

import kr.co.inslab.model.Error;
import kr.co.inslab.model.NewProject;
import kr.co.inslab.model.Project;
import kr.co.inslab.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-03-30T15:23:45.786+09:00[Asia/Seoul]")
@Controller
public class UsersApiController implements UsersApi {

    private static final Logger log = LoggerFactory.getLogger(UsersApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public UsersApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<User> usersGet(@ApiParam(value = "프로젝트 정보까지 포함") @Valid @RequestParam(value = "include_project", required = false) Boolean includeProject
) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<User>(objectMapper.readValue("{\n  \"email_verified\" : true,\n  \"projects\" : [ {\n    \"owner\" : \"owner\",\n    \"pending_users\" : [ {\n      \"email\" : \"email\"\n    }, {\n      \"email\" : \"email\"\n    } ],\n    \"name\" : \"name\",\n    \"description\" : \"description\",\n    \"groups\" : [ {\n      \"members\" : [ {\n        \"email_verified\" : true,\n        \"user_id\" : \"user_id\",\n        \"user_name\" : \"user_name\",\n        \"email\" : \"email\"\n      }, {\n        \"email_verified\" : true,\n        \"user_id\" : \"user_id\",\n        \"user_name\" : \"user_name\",\n        \"email\" : \"email\"\n      } ],\n      \"name\" : \"name\",\n      \"id\" : \"id\",\n      \"display_name\" : \"display_name\"\n    }, {\n      \"members\" : [ {\n        \"email_verified\" : true,\n        \"user_id\" : \"user_id\",\n        \"user_name\" : \"user_name\",\n        \"email\" : \"email\"\n      }, {\n        \"email_verified\" : true,\n        \"user_id\" : \"user_id\",\n        \"user_name\" : \"user_name\",\n        \"email\" : \"email\"\n      } ],\n      \"name\" : \"name\",\n      \"id\" : \"id\",\n      \"display_name\" : \"display_name\"\n    } ],\n    \"id\" : \"id\",\n    \"display_name\" : \"display_name\",\n    \"status\" : \"archive\"\n  }, {\n    \"owner\" : \"owner\",\n    \"pending_users\" : [ {\n      \"email\" : \"email\"\n    }, {\n      \"email\" : \"email\"\n    } ],\n    \"name\" : \"name\",\n    \"description\" : \"description\",\n    \"groups\" : [ {\n      \"members\" : [ {\n        \"email_verified\" : true,\n        \"user_id\" : \"user_id\",\n        \"user_name\" : \"user_name\",\n        \"email\" : \"email\"\n      }, {\n        \"email_verified\" : true,\n        \"user_id\" : \"user_id\",\n        \"user_name\" : \"user_name\",\n        \"email\" : \"email\"\n      } ],\n      \"name\" : \"name\",\n      \"id\" : \"id\",\n      \"display_name\" : \"display_name\"\n    }, {\n      \"members\" : [ {\n        \"email_verified\" : true,\n        \"user_id\" : \"user_id\",\n        \"user_name\" : \"user_name\",\n        \"email\" : \"email\"\n      }, {\n        \"email_verified\" : true,\n        \"user_id\" : \"user_id\",\n        \"user_name\" : \"user_name\",\n        \"email\" : \"email\"\n      } ],\n      \"name\" : \"name\",\n      \"id\" : \"id\",\n      \"display_name\" : \"display_name\"\n    } ],\n    \"id\" : \"id\",\n    \"display_name\" : \"display_name\",\n    \"status\" : \"archive\"\n  } ],\n  \"user_id\" : \"user_id\",\n  \"user_name\" : \"user_name\",\n  \"last_name\" : \"last_name\",\n  \"first_name\" : \"first_name\",\n  \"email\" : \"email\"\n}", User.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<User>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<User>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Project> usersProjectsPost(@ApiParam(value = "" ,required=true )  @Valid @RequestBody NewProject body
) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<Project>(objectMapper.readValue("{\n  \"owner\" : \"owner\",\n  \"pending_users\" : [ {\n    \"email\" : \"email\"\n  }, {\n    \"email\" : \"email\"\n  } ],\n  \"name\" : \"name\",\n  \"description\" : \"description\",\n  \"groups\" : [ {\n    \"members\" : [ {\n      \"email_verified\" : true,\n      \"user_id\" : \"user_id\",\n      \"user_name\" : \"user_name\",\n      \"email\" : \"email\"\n    }, {\n      \"email_verified\" : true,\n      \"user_id\" : \"user_id\",\n      \"user_name\" : \"user_name\",\n      \"email\" : \"email\"\n    } ],\n    \"name\" : \"name\",\n    \"id\" : \"id\",\n    \"display_name\" : \"display_name\"\n  }, {\n    \"members\" : [ {\n      \"email_verified\" : true,\n      \"user_id\" : \"user_id\",\n      \"user_name\" : \"user_name\",\n      \"email\" : \"email\"\n    }, {\n      \"email_verified\" : true,\n      \"user_id\" : \"user_id\",\n      \"user_name\" : \"user_name\",\n      \"email\" : \"email\"\n    } ],\n    \"name\" : \"name\",\n    \"id\" : \"id\",\n    \"display_name\" : \"display_name\"\n  } ],\n  \"id\" : \"id\",\n  \"display_name\" : \"display_name\",\n  \"status\" : \"archive\"\n}", Project.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<Project>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<Project>(HttpStatus.NOT_IMPLEMENTED);
    }

}
