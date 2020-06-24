package kr.co.inslab;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.inslab.api.ProjectsApiController;
import kr.co.inslab.api.UsersApiController;
import kr.co.inslab.model.*;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class ProjectApiTest {

    private static final Logger log = LoggerFactory.getLogger(ProjectApiTest.class);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProjectsApiController projectsApiController;

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

    public static String projectId;

    public static String groupId;


    @Test
    @Order(0)
    public void contextLoads() {
        assertThat(projectsApiController).isNotNull();
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

    @Test
    @Order(3)
    public void createProject() throws Exception {

        NewProject newProject = new NewProject();
        newProject.setDescription("test");
        newProject.setDisplayName("project_api_test");


        ObjectMapper objectMapper = new ObjectMapper();
        String newProjectStr = objectMapper.writeValueAsString(newProject);

        MvcResult mvcResult = this.mockMvc.perform(post("/users/projects")
                .header(AUTHORIZATION, BEARER + accessToken)
                .content(newProjectStr)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        Project project = objectMapper.readValue(content, Project.class);

        String displayName = project.getDisplayName();

        assertThat(displayName).isEqualTo("project_api_test");

        projectId = project.getId();

    }


    @Test
    @Order(4)
    public void getProjectInfoById() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(get("/projects/"+projectId)
                .header(AUTHORIZATION, BEARER + accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is2xxSuccessful()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        Project project = objectMapper.readValue(content, Project.class);

        String projectDisplayName = project.getDisplayName();
        assertThat(projectDisplayName).isEqualTo("project_api_test");

    }

    @Test
    @Order(5)
    public void updateProjectInfo() throws Exception {

        UpdateProject updateProject = new UpdateProject();
        updateProject.setDescription("change description");


        ObjectMapper objectMapper = new ObjectMapper();
        String updateProjectStr = objectMapper.writeValueAsString(updateProject);

        this.mockMvc.perform(patch("/projects/"+projectId)
                .header(AUTHORIZATION, BEARER + accessToken)
                .content(updateProjectStr)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful()).andReturn();

    }

    @Test
    @Order(6)
    public void archiveProject() throws Exception {

        this.mockMvc.perform(put("/projects/"+projectId+"/archive")
                .header(AUTHORIZATION, BEARER + accessToken)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is2xxSuccessful()).andReturn();

    }

    @Test
    @Order(7)
    public void activeProject() throws Exception {

        this.mockMvc.perform(put("/projects/"+projectId+"/active")
                .header(AUTHORIZATION, BEARER + accessToken)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is2xxSuccessful()).andReturn();

    }


    @Test
    @Order(8)
    public void getProjectGroupInfoById() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(get("/projects/"+projectId+"/groups")
                .header(AUTHORIZATION, BEARER + accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is2xxSuccessful()).andReturn();
        String content = mvcResult.getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        List<Group> groups = objectMapper.readValue(content,objectMapper.getTypeFactory().constructCollectionType(List.class, Group.class));

        for(Group group : groups){
            assertThat(group.getId()).isNotEmpty();

        }
        groupId = groups.get(0).getId();
    }


// 비동기 테스트 제외
//    @Test
//    @Order(9)
//    public void inviteMember() throws Exception {
//        MvcResult mvcResult = this.mockMvc.perform(put("/projects/"+projectId+"/groups/"+groupId+"/invitation")
//                .param("email","echan1020@inslab.co.kr")
//                .header(AUTHORIZATION, BEARER + accessToken)
//                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
//                .andExpect(status().is2xxSuccessful()).andReturn();
//    }

//    @Test
//    @Order(10)
//    public void cancelInvitedMember() throws Exception {
//        MvcResult mvcResult = this.mockMvc.perform(delete("/projects/"+projectId+"/groups/invitation")
//                .param("email","echan1020@inslab.co.kr")
//                .header(AUTHORIZATION, BEARER + accessToken)
//                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
//                .andExpect(status().is2xxSuccessful()).andReturn();
//    }

    @Test
    @Order(11)
    public void getMembersInGroup() throws Exception {

       this.mockMvc.perform(get("/projects/"+projectId+"/groups/"+groupId+"/members")
                .header(AUTHORIZATION, BEARER + accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is2xxSuccessful()).andReturn();

    }

    @Test
    @Order(12)
    public void deleteProjectById() throws Exception {

        MvcResult mvcResult = this.mockMvc.perform(delete("/projects/"+projectId)
                .header(AUTHORIZATION, BEARER + accessToken)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is2xxSuccessful()).andReturn();
    }
}

