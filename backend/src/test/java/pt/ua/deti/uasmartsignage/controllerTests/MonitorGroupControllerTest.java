package pt.ua.deti.uasmartsignage.controllerTests;

import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import pt.ua.deti.uasmartsignage.Models.Template;
import pt.ua.deti.uasmartsignage.Models.TemplateGroup;
import pt.ua.deti.uasmartsignage.Services.CustomUserDetailsService;
import pt.ua.deti.uasmartsignage.Services.JwtUtilService;
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

import pt.ua.deti.uasmartsignage.Models.Monitor;
import pt.ua.deti.uasmartsignage.Controllers.MonitorGroupController;
import pt.ua.deti.uasmartsignage.Services.MonitorGroupService;
import pt.ua.deti.uasmartsignage.Models.MonitorsGroup;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MonitorGroupController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
class MonitorGroupControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MonitorGroupService service;

    @MockBean
    private CustomUserDetailsService userDetailsService;

    @MockBean
    private JwtUtilService jwtUtil;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGetAllMonitorsEndpoint() throws Exception{

        Monitor monitor = new Monitor();
        monitor.setName("hall");
        monitor.setPending(false);

        Monitor monitor2 = new Monitor();
        monitor2.setName("deti");
        monitor2.setPending(false);

        MonitorsGroup deti = new MonitorsGroup();
        deti.setName("DETI");
        deti.setDescription("Monitors from first floor, second and third");
        deti.setMonitors(List.of(monitor, monitor2));

        MonitorsGroup hall = new MonitorsGroup();
        hall.setName("hall");
        hall.setDescription("Monitors from first floor, second and third");
        hall.setMonitors(List.of(monitor));

        when(service.getAllGroups()).thenReturn(Arrays.asList(deti, hall));

        mvc.perform(get("/api/groups").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("DETI")))
                .andExpect(jsonPath("$[1].name", is("hall")));
    }

    @Test
    void testGetMonitorsGroupsThatAreNotAssociatedWithOnlyOneMonitor() throws Exception{

        Monitor monitor = new Monitor();
        monitor.setName("monitor1");
        monitor.setId(1L);

        Monitor monitor2 = new Monitor();
        monitor2.setName("monitor2");
        monitor2.setId(2L);

        Monitor monitor3 = new Monitor();
        monitor3.setName("monitor3");
        monitor3.setId(3L);

        MonitorsGroup monitorsGroup = new MonitorsGroup();
        monitorsGroup.setName("group1");
        monitorsGroup.setId(1L);
        monitorsGroup.setMadeForMonitor(false);
        monitorsGroup.setMonitors(Arrays.asList(monitor, monitor3));

        MonitorsGroup monitorsGroup2 = new MonitorsGroup();
        monitorsGroup2.setName("group2");
        monitorsGroup2.setId(2L);
        monitorsGroup2.setMadeForMonitor(true);
        monitorsGroup2.setMonitors(List.of(monitor2));

        MonitorsGroup monitorsGroup3 = new MonitorsGroup();
        monitorsGroup3.setName("group3");
        monitorsGroup3.setId(3L);
        monitorsGroup3.setMadeForMonitor(false);
        monitorsGroup3.setMonitors(Arrays.asList(monitor, monitor2, monitor3));

        when(service.getAllGroupsNotMadeForMonitor()).thenReturn(List.of(monitorsGroup, monitorsGroup3));

        mvc.perform(get("/api/groups/notMadeForMonitor").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("group1")))
                .andExpect(jsonPath("$[1].name", is("group3")));
    }

    @Test
    void testGetGroupByIdEndpoint() throws Exception{

        Monitor monitor = new Monitor();
        monitor.setName("monitor1");
        monitor.setId(1L);

        Monitor monitor2 = new Monitor();
        monitor2.setName("monitor2");
        monitor2.setId(2L);

        MonitorsGroup monitorsGroup = new MonitorsGroup();
        monitorsGroup.setName("group1");
        monitorsGroup.setId(1L);
        monitorsGroup.setMadeForMonitor(false);
        monitorsGroup.setMonitors(Arrays.asList(monitor, monitor2));

        when(service.getGroupById(1L)).thenReturn(monitorsGroup);

        mvc.perform(get("/api/groups/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(monitorsGroup.getName())))
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void testGetGroupByNameEndpoint() throws Exception{

        Monitor monitor = new Monitor();
        monitor.setName("monitor1");
        monitor.setId(1L);

        Monitor monitor2 = new Monitor();
        monitor2.setName("monitor2");
        monitor2.setId(2L);

        MonitorsGroup monitorsGroup = new MonitorsGroup();
        monitorsGroup.setName("group1");
        monitorsGroup.setId(1L);
        monitorsGroup.setMadeForMonitor(false);
        monitorsGroup.setMonitors(Arrays.asList(monitor, monitor2));

        when(service.getGroupByName("group1")).thenReturn(monitorsGroup);

        mvc.perform(get("/api/groups/name/group1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(monitorsGroup.getName())))
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    void testGetAllMonitorsFromGroupEndpoint() throws Exception{
        Monitor monitor = new Monitor();
        monitor.setName("monitor1");
        monitor.setId(1L);

        Monitor monitor2 = new Monitor();
        monitor2.setName("monitor2");
        monitor2.setId(2L);

        MonitorsGroup monitorsGroup = new MonitorsGroup();
        monitorsGroup.setName("group1");
        monitorsGroup.setId(1L);
        monitorsGroup.setMadeForMonitor(false);
        monitorsGroup.setMonitors(Arrays.asList(monitor, monitor2));

        when(service.getGroupById(1L)).thenReturn(monitorsGroup);

        mvc.perform(get("/api/groups/1/monitors")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("monitor1")))
                .andExpect(jsonPath("$[1].name", is("monitor2")));
    }

    @Test
    void testGetTemplateFromGroup() throws Exception{
        Monitor monitor = new Monitor();
        monitor.setName("monitor1");
        monitor.setId(1L);

        Monitor monitor2 = new Monitor();
        monitor2.setName("monitor2");
        monitor2.setId(2L);

        Template template = new Template();
        template.setName("template1");

        TemplateGroup templateGroup = new TemplateGroup();
        templateGroup.setTemplate(template);

        MonitorsGroup monitorsGroup = new MonitorsGroup();
        monitorsGroup.setName("group1");
        monitorsGroup.setId(1L);
        monitorsGroup.setMadeForMonitor(false);
        monitorsGroup.setTemplateGroups(List.of(templateGroup));
        monitorsGroup.setMonitors(Arrays.asList(monitor, monitor2));

        when(service.getGroupById(1L)).thenReturn(monitorsGroup);

        mvc.perform(get("/api/groups/1/template")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].template.name", is("template1")));
    }

    @Test
    void testCreateGroup() throws Exception{
        Monitor monitor = new Monitor();
        monitor.setName("monitor1");
        monitor.setId(1L);

        Monitor monitor2 = new Monitor();
        monitor2.setName("monitor2");
        monitor2.setId(2L);


        MonitorsGroup monitorsGroup = new MonitorsGroup();
        monitorsGroup.setName("group1");
        monitorsGroup.setId(1L);
        monitorsGroup.setMadeForMonitor(false);
        monitorsGroup.setMonitors(Arrays.asList(monitor, monitor2));

        when(service.saveGroup(Mockito.any())).thenReturn(monitorsGroup);

        mvc.perform(post("/api/groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(monitorsGroup)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("group1")))
                .andExpect(jsonPath("$.madeForMonitor", is(false)));
    }

    @Test
    void testUpdateGroup() throws Exception{
        Monitor monitor = new Monitor();
        monitor.setName("monitor1");
        monitor.setId(1L);

        Monitor monitor2 = new Monitor();
        monitor2.setName("monitor2");
        monitor2.setId(2L);

        MonitorsGroup monitorsGroup = new MonitorsGroup();
        monitorsGroup.setName("group1");
        monitorsGroup.setId(1L);
        monitorsGroup.setMadeForMonitor(false);
        monitorsGroup.setMonitors(Arrays.asList(monitor, monitor2));

        when(service.getGroupById(1L)).thenReturn(monitorsGroup);
        when(service.updateGroup(Mockito.anyLong(),Mockito.any())).thenReturn(monitorsGroup);

        mvc.perform(put("/api/groups/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(monitorsGroup)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("group1")));
    }

    @Test
    void testDeleteGroup() throws Exception{
        Monitor monitor = new Monitor();
        monitor.setName("monitor1");
        monitor.setId(1L);

        Monitor monitor2 = new Monitor();
        monitor2.setName("monitor2");
        monitor2.setId(2L);

        MonitorsGroup monitorsGroup = new MonitorsGroup();
        monitorsGroup.setName("group1");
        monitorsGroup.setId(1L);
        monitorsGroup.setMadeForMonitor(false);
        monitorsGroup.setMonitors(Arrays.asList(monitor, monitor2));

        when(service.getGroupById(1L)).thenReturn(monitorsGroup);

        mvc.perform(delete("/api/groups/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }



}



