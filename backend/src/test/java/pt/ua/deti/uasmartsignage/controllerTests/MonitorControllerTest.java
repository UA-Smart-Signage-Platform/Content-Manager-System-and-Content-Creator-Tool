package pt.ua.deti.uasmartsignage.controllerTests;

import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import pt.ua.deti.uasmartsignage.controllers.MonitorController;
import pt.ua.deti.uasmartsignage.models.Monitor;
import pt.ua.deti.uasmartsignage.services.CustomUserDetailsService;
import pt.ua.deti.uasmartsignage.services.MonitorService;
import pt.ua.deti.uasmartsignage.services.JwtUtilService;
import pt.ua.deti.uasmartsignage.authentication.IAuthenticationFacade;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MonitorController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
class MonitorControllerTest {
    
    @Autowired
    private MockMvc mvc;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private JwtUtilService jwtUtil;

    @MockBean
    private IAuthenticationFacade authenticationFacade;

    @MockBean 
    private MonitorService service;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test void
    testGetAllMonitorsEndpoint() throws Exception{
        Monitor monitor = new Monitor();
        monitor.setName("hall");
        monitor.setPending(false);

        Monitor monitor2 = new Monitor();
        monitor2.setName("deti");
        monitor2.setPending(false);

        when(service.getAllMonitorsByPending(false)).thenReturn(Arrays.asList(monitor,monitor2));

        mvc.perform(get("/api/monitors").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].name", is("hall")))
            .andExpect(jsonPath("$[1].name", is("deti")));
    }

    @Test void
    testGetAllMonitorsEndpointWhereOnlineStatusDifferentNull() throws Exception{
        Monitor monitor = new Monitor();
        monitor.setName("hall");
        monitor.setPending(false);

        Monitor monitor2 = new Monitor();
        monitor2.setName("deti");
        monitor2.setPending(false);
        monitor2.setOnline(true);

        when(service.getAllMonitorsByPendingAndOnline(false, true)).thenReturn(Arrays.asList(monitor2));

        mvc.perform(get("/api/monitors?onlineStatus=true").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].name", is("deti")));
    }

    @Test void
    testGetAllPendingMonitors() throws Exception{
        Monitor monitor = new Monitor();
        monitor.setName("hall");
        monitor.setPending(true);

        Monitor monitor2 = new Monitor();
        monitor2.setName("deti");
        monitor2.setPending(true);

        when(service.getAllMonitorsByPending(true)).thenReturn(Arrays.asList(monitor,monitor2));

        mvc.perform(get("/api/monitors/pending").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].name", is("hall")))
            .andExpect(jsonPath("$[1].name", is("deti")));
    }

    @Test void
    testAcceptingPendingMonitor200() throws Exception{
        Monitor monitor = new Monitor();
        monitor.setId(1L);
        monitor.setName("hall");
        monitor.setPending(false);

        when(service.updatePending(1L, false)).thenReturn(monitor);

        mvc.perform(put("/api/monitors/accept/1").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is("hall")));
    }

    @Test void
    testAcceptingPendingMonitor404() throws Exception{

        when(service.updatePending(1L, false)).thenReturn(null);

        mvc.perform(put("/api/monitors/accept/1").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test void
    testGetMonitorById200() throws Exception{
        Monitor monitor = new Monitor();
        monitor.setId(1L);
        monitor.setName("hall");
        monitor.setPending(false);

        when(service.getMonitorById(1L)).thenReturn(Optional.of(monitor));

        mvc.perform(get("/api/monitors/1").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is("hall")));
    }

    @Test void
    testGetMonitorById404() throws Exception{

        when(service.getMonitorById(1L)).thenReturn(Optional.empty());

        mvc.perform(get("/api/monitors/1").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test void
    testGetMonitorsByGroup() throws Exception{
        Monitor monitor = new Monitor();
        monitor.setName("hall");
        monitor.setPending(false);

        Monitor monitor2 = new Monitor();
        monitor2.setName("deti");
        monitor2.setPending(false);

        when(service.getMonitorsByGroup(1L)).thenReturn(Arrays.asList(monitor,monitor2));

        mvc.perform(get("/api/monitors/group/1").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].name", is("hall")))
            .andExpect(jsonPath("$[1].name", is("deti")));
    }

    @Test void
    testSaveMonitor200() throws Exception{
        Monitor monitor = new Monitor();
        monitor.setName("hall");
        monitor.setPending(false);

        when(service.saveMonitor(Mockito.any())).thenReturn(monitor);

        mvc.perform(post("/api/monitors").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(monitor)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name", is("hall")));
    }

    @Test void
    testSaveMonitor400() throws Exception{

        mvc.perform(post("/api/monitors").contentType(MediaType.APPLICATION_JSON).content("monitor"))
            .andExpect(status().isBadRequest());
    }

    @Test void
    testUpdateMonitor200() throws Exception{
        Monitor monitor = new Monitor();
        monitor.setName("hall");
        monitor.setPending(false);

        when(service.updateMonitor(Mockito.anyLong(), Mockito.any())).thenReturn(monitor);

        mvc.perform(put("/api/monitors/1").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(monitor)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is("hall")));
    }

    @Test void
    testUpdateMonitor404() throws Exception{
        Monitor monitor = new Monitor();
        monitor.setName("hall");
        monitor.setPending(false);

        when(service.updateMonitor(Mockito.anyLong(), Mockito.any())).thenReturn(null);

        mvc.perform(put("/api/monitors/1").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(monitor)))
            .andExpect(status().isNotFound());
    }

    @Test void
    testDeleteMonitor() throws Exception{
        when(service.deleteMonitor(1L)).thenReturn(true);

        mvc.perform(delete("/api/monitors/1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());
    }

    
}
