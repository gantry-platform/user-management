package kr.co.inslab;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.inslab.api.UsersApiController;
import kr.co.inslab.model.NewUser;
import kr.co.inslab.model.Token;
import kr.co.inslab.model.UpdateUser;
import kr.co.inslab.model.User;
import kr.co.inslab.utils.CommonConstants;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class UsersApiTest {

    private static final Logger log = LoggerFactory.getLogger(UsersApiTest.class);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsersApiController usersApiController;

    @Value("${keycloak.testClientId}")
    private String TEST_CLIENT_ID;

    @Value("${keycloak.testClientSecret}")
    private String TEST_CLIENT_SECRET;

    @Value("${keycloak.testUserName}")
    private String TEST_USER_NAME;

    @Value("${keycloak.testUserEmail}")
    private String TEST_USER_EMAIL;

    @Value("${keycloak.testUserPass}")
    private String TEST_USER_PASS;

    public static String accessToken;

    public static String userId;

    public static String AUTHORIZATION = "Authorization";

    public static String BEARER = "Bearer ";


    @Test
    @Order(0)
    public void contextLoads() {
        assertThat(usersApiController).isNotNull();
    }

    @Test
    @Order(1)
    public void createUser() throws Exception {

        NewUser newUser = new NewUser();
        newUser.setEmail(TEST_USER_EMAIL);
        newUser.setPassword(TEST_USER_PASS);
        newUser.setUserName(TEST_USER_NAME);
        ObjectMapper objectMapper = new ObjectMapper();
        String newUserStr = objectMapper.writeValueAsString(newUser);

         this.mockMvc.perform(post("/test/users")
                 .content(newUserStr)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful()).andReturn();
    }


    @Test
    @Order(2)
    public void getToken() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(post("/token")
                .param(CommonConstants.USERNAME,TEST_USER_NAME)
                .param("password",TEST_USER_PASS)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is2xxSuccessful()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        Token token = objectMapper.readValue(content, Token.class);

        accessToken = token.getAccessToken();
        DecodedJWT decodedJWT = JWT.decode(accessToken);
        userId = decodedJWT.getSubject();
    }

    @Test
    @Order(3)
    public void gerUserInfo() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(get("/users/"+userId)
                .header(AUTHORIZATION, BEARER + accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is2xxSuccessful()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        User user = objectMapper.readValue(content, User.class);

        String name = user.getUserName();
        assertThat(name).isEqualTo(TEST_USER_NAME);

    }

    @Test
    @Order(4)
    public void updateUser() throws Exception {

        UpdateUser updateUser = new UpdateUser();
        updateUser.setFirstName("ChanHO");
        updateUser.setLastName("Lee");
        ObjectMapper objectMapper = new ObjectMapper();
        String updateUserStr = objectMapper.writeValueAsString(updateUser);

         this.mockMvc.perform(put("/users/"+userId)
                .header(AUTHORIZATION, BEARER + accessToken)
                .content(updateUserStr)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful()).andReturn();

        MvcResult mvcResult = this.mockMvc.perform(get("/users/"+userId)
                .header(AUTHORIZATION, BEARER + accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is2xxSuccessful()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();

        ObjectMapper resultObjectMapper = new ObjectMapper();
        User user = resultObjectMapper.readValue(content, User.class);

        String firstName = user.getFirstName();
        assertThat(firstName).isEqualTo("ChanHO");
    }

    @Test
    @Order(5)
    public void deleteUser() throws Exception {

        this.mockMvc.perform(delete("/users/"+userId)
                .header(AUTHORIZATION, BEARER + accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is2xxSuccessful()).andReturn();

        this.mockMvc.perform(get("/users/"+userId)
                .header(AUTHORIZATION, BEARER + accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is2xxSuccessful()).andReturn();

        this.mockMvc.perform(delete("/test/users/"+userId)
                .header(AUTHORIZATION, BEARER + accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is2xxSuccessful()).andReturn();
    }

}

