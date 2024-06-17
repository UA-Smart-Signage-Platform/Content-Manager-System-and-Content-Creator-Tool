package deti.uas.uasmartsignage.controllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import deti.uas.uasmartsignage.Configuration.JwtAuthFilter;
import deti.uas.uasmartsignage.Controllers.UserController;
import deti.uas.uasmartsignage.Models.AppUser;
import deti.uas.uasmartsignage.Services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import static org.mockito.Mockito.when;

@WebMvcTest(UserController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService service;

    @MockBean
    private JwtAuthFilter jwtAuthFilter;


    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGetAllUsers() throws Exception {
        AppUser user = new AppUser();
        user.setEmail("asdfgh");
        user.setPassword("123456");
        user.setRole("admin");

        AppUser user2 = new AppUser();
        user2.setEmail("qwerty");
        user.setRole("user");

        when(service.getAllUsers()).thenReturn(List.of(user, user2));

        mvc.perform(get("/api/users"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].email", is("asdfgh")))
            .andExpect(jsonPath("$[1].email", is("qwerty")));
    }

    @Test
    void testCreateUser() throws Exception{
        AppUser user = new AppUser();
        user.setEmail("asdfgh");
        user.setPassword("123456");
        user.setRole("ADMIN");

        when(service.saveUser(Mockito.any(AppUser.class))).thenReturn(user);

       mvc.perform(post("/api/users")
                       .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(user)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.email", is("asdfgh")))
                       .andExpect(jsonPath("$.role", is("ADMIN")));
    }

    @Test
    void testDeleteUser() throws Exception {
        AppUser user = new AppUser();
        user.setEmail("asdfgh");
        user.setPassword("123456");
        user.setRole("ADMIN");

        mvc.perform(delete("/api/users/1"))
            .andExpect(status().isOk());
    }

}
