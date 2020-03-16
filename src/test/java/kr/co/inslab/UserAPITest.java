//package kr.co.inslab;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import kr.co.inslab.model.UserInvitation;
//import kr.co.inslab.service.UserService;
//import org.junit.Before;
//import org.junit.FixMethodOrder;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.junit.runners.MethodSorters;
//import org.keycloak.admin.client.resource.UserResource;
//import org.keycloak.representations.idm.UserRepresentation;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.List;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureMockMvc
//@FixMethodOrder(MethodSorters.DEFAULT)
//public class UserAPITest {
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private UserService userService;
//
//    private UserInvitation userInvitation;
//
//    static String id;
//
//    @Before
//    public void setup(){
//        userInvitation = new UserInvitation();
//        userInvitation.setEamil("echan1020@gmail.com");
//    }
//
//    @Test
//    public void inviteUser1() throws Exception{
//
//        this.mockMvc.perform(post("/users/invitation")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(userInvitation))).andExpect(status().isCreated());
//
//        List<UserRepresentation> invitedUser = userService.getUserByEmail(userInvitation.getEamil());
//        UserResource resource= userService.getUserResourceById(invitedUser.get(0).getId());
//        UserRepresentation userRepresentation = resource.toRepresentation();
//        id = userRepresentation.getId();
//    }
//
//    @Test
//    public void deleteUser2() throws Exception{
//        this.mockMvc.perform(delete("/users/{id}",id)).andExpect(status().isOk());
//
//    }
//
//    public static String asJsonString(final Object obj) {
//        try {
//            return new ObjectMapper().writeValueAsString(obj);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//}
