package kr.co.inslab;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.inslab.api.UsersApiController;
import kr.co.inslab.model.Token;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @Value("${keycloak.testUserPass}")
    private String TEST_USER_PASS;

    public static String accessToken;

    public static String AUTHORIZATION = "Authorization";

    public static String BEARER = "Bearer ";


    @Test
    @Order(0)
    public void contextLoads() {
        assertThat(usersApiController).isNotNull();
    }


    @Test
    @Order(1)
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
        log.debug(accessToken);

    }

    @Test
    @Order(2)
    public void gerUserInfo() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(get("/users")
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
}

