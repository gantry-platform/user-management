package kr.co.inslab.api.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.inslab.api.UsersApiController;
import kr.co.inslab.gantry.GantryUser;
import kr.co.inslab.model.NewUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


@Profile("test")
@Controller
public class TestUserApiController implements TestUserApi {

    private static final Logger log = LoggerFactory.getLogger(UsersApiController.class);

    private final GantryUser gantryUser;

    @org.springframework.beans.factory.annotation.Autowired
    public TestUserApiController(ObjectMapper objectMapper, HttpServletRequest request, GantryUser gantryUser) {
        this.gantryUser = gantryUser;
    }

    @Override
    public ResponseEntity<Void> usersPost(@Valid NewUser body) throws Exception {
        this.gantryUser.createTesUser(body);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> usersUserIdDelete(String userId) throws Exception {
        this.gantryUser.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
