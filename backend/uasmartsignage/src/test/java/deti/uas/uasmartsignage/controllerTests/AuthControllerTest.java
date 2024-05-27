package deti.uas.uasmartsignage.controllerTests;


import com.fasterxml.jackson.databind.ObjectMapper;
import deti.uas.uasmartsignage.Configuration.JwtAuthFilter;
import deti.uas.uasmartsignage.Controllers.AuthController;
import deti.uas.uasmartsignage.Models.AppUser;
import deti.uas.uasmartsignage.Services.CustomUserDetailsService;
import deti.uas.uasmartsignage.Services.JwtUtilService;
import deti.uas.uasmartsignage.Services.UserService;
import deti.uas.uasmartsignage.authentication.ChangePasswordRequest;
import deti.uas.uasmartsignage.authentication.IAuthenticationFacade;
import org.junit.jupiter.api.Disabled;
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
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import static org.mockito.Mockito.when;

@WebMvcTest(AuthController.class)
@ActiveProfiles("test")
//@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private CustomUserDetailsService service;

    @MockBean
    private IAuthenticationFacade authenticationFacade;

    @MockBean
    private JwtUtilService jwtUtilService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @Disabled
    void testChangePassword() throws Exception{
        AppUser user = new AppUser();
        user.setEmail("ola");
        user.setPassword("123456");
        user.setRole("admin");

        ChangePasswordRequest op = new ChangePasswordRequest("ola", "123456", "1234");

        //when(service.updateUserPassword(op.getUsername(),op.getNewPassword())).thenReturn(null);

        mvc.perform(post("/api/login/change-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(op)))
                .andExpect(status().isOk());


    }

}
