// package pt.ua.deti.uasmartsignage.controllerTests;

// import static org.mockito.Mockito.when;

// import java.util.Arrays;
// import java.util.List;

// import pt.ua.deti.uasmartsignage.models.Template;
// import pt.ua.deti.uasmartsignage.services.CustomUserDetailsService;
// import pt.ua.deti.uasmartsignage.services.JwtUtilService;
// import org.junit.jupiter.api.Test;
// import org.mockito.Mockito;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.http.MediaType;
// import org.springframework.test.context.ActiveProfiles;
// import org.springframework.test.web.servlet.MockMvc;

// import com.fasterxml.jackson.databind.ObjectMapper;

// import static org.hamcrest.Matchers.hasSize;
// import static org.hamcrest.Matchers.is;

// import pt.ua.deti.uasmartsignage.models.Monitor;
// import pt.ua.deti.uasmartsignage.controllers.MonitorGroupController;
// import pt.ua.deti.uasmartsignage.services.MonitorGroupService;
// import pt.ua.deti.uasmartsignage.models.MonitorGroup;


// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// @WebMvcTest(MonitorGroupController.class)
// @ActiveProfiles("test")
// @AutoConfigureMockMvc(addFilters = false)
// class MonitorGroupControllerTest {

//     @Autowired
//     private MockMvc mvc;

//     @MockBean
//     private MonitorGroupService service;

//     @MockBean
//     private CustomUserDetailsService userDetailsService;

//     @MockBean
//     private JwtUtilService jwtUtil;

//     private ObjectMapper objectMapper = new ObjectMapper();

//     @Test
//     void testGetAllMonitorsEndpoint() throws Exception{

//         Monitor monitor = new Monitor();
//         monitor.setName("hall");
//         monitor.setPending(false);

//         Monitor monitor2 = new Monitor();
//         monitor2.setName("deti");
//         monitor2.setPending(false);

//         MonitorGroup deti = new MonitorGroup();
//         deti.setName("DETI");
//         deti.setDescription("Monitors from first floor, second and third");
//         deti.setMonitors(List.of(monitor, monitor2));

//         MonitorGroup hall = new MonitorGroup();
//         hall.setName("hall");
//         hall.setDescription("Monitors from first floor, second and third");
//         hall.setMonitors(List.of(monitor));

//         when(service.getAllGroups()).thenReturn(Arrays.asList(deti, hall));

//         mvc.perform(get("/api/groups").contentType(MediaType.APPLICATION_JSON))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$", hasSize(2)))
//                 .andExpect(jsonPath("$[0].name", is("DETI")))
//                 .andExpect(jsonPath("$[1].name", is("hall")));
//     }

//     @Test
//     void testGetMonitorGroupsThatAreNotAssociatedWithOnlyOneMonitor() throws Exception{

//         Monitor monitor = new Monitor();
//         monitor.setName("monitor1");
//         monitor.setId(1L);

//         Monitor monitor2 = new Monitor();
//         monitor2.setName("monitor2");
//         monitor2.setId(2L);

//         Monitor monitor3 = new Monitor();
//         monitor3.setName("monitor3");
//         monitor3.setId(3L);

//         MonitorGroup MonitorGroup = new MonitorGroup();
//         MonitorGroup.setName("group1");
//         MonitorGroup.setId(1L);
//         MonitorGroup.setDefaultGroup(false);
//         MonitorGroup.setMonitors(Arrays.asList(monitor, monitor3));

//         MonitorGroup MonitorGroup2 = new MonitorGroup();
//         MonitorGroup2.setName("group2");
//         MonitorGroup2.setId(2L);
//         MonitorGroup2.setDefaultGroup(true);
//         MonitorGroup2.setMonitors(List.of(monitor2));

//         MonitorGroup MonitorGroup3 = new MonitorGroup();
//         MonitorGroup3.setName("group3");
//         MonitorGroup3.setId(3L);
//         MonitorGroup3.setDefaultGroup(false);
//         MonitorGroup3.setMonitors(Arrays.asList(monitor, monitor2, monitor3));

//         when(service.getAllNonDefaultGroups()).thenReturn(List.of(MonitorGroup, MonitorGroup3));

//         mvc.perform(get("/api/groups/notMadeForMonitor").contentType(MediaType.APPLICATION_JSON))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$", hasSize(2)))
//                 .andExpect(jsonPath("$[0].name", is("group1")))
//                 .andExpect(jsonPath("$[1].name", is("group3")));
//     }

//     @Test
//     void testGetGroupByIdEndpoint() throws Exception{

//         Monitor monitor = new Monitor();
//         monitor.setName("monitor1");
//         monitor.setId(1L);

//         Monitor monitor2 = new Monitor();
//         monitor2.setName("monitor2");
//         monitor2.setId(2L);

//         MonitorGroup MonitorGroup = new MonitorGroup();
//         MonitorGroup.setName("group1");
//         MonitorGroup.setId(1L);
//         MonitorGroup.setDefaultGroup(false);
//         MonitorGroup.setMonitors(Arrays.asList(monitor, monitor2));

//         when(service.getGroupById(1L)).thenReturn(MonitorGroup);

//         mvc.perform(get("/api/groups/1")
//                         .contentType(MediaType.APPLICATION_JSON))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.name", is(MonitorGroup.getName())))
//                 .andExpect(jsonPath("$.id", is(1)));
//     }

//     @Test
//     void testGetGroupByNameEndpoint() throws Exception{

//         Monitor monitor = new Monitor();
//         monitor.setName("monitor1");
//         monitor.setId(1L);

//         Monitor monitor2 = new Monitor();
//         monitor2.setName("monitor2");
//         monitor2.setId(2L);

//         MonitorGroup MonitorGroup = new MonitorGroup();
//         MonitorGroup.setName("group1");
//         MonitorGroup.setId(1L);
//         MonitorGroup.setDefaultGroup(false);
//         MonitorGroup.setMonitors(Arrays.asList(monitor, monitor2));

//         when(service.getGroupByName("group1")).thenReturn(MonitorGroup);

//         mvc.perform(get("/api/groups/name/group1")
//                         .contentType(MediaType.APPLICATION_JSON))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.name", is(MonitorGroup.getName())))
//                 .andExpect(jsonPath("$.id", is(1)));
//     }

//     @Test
//     void testGetAllMonitorsFromGroupEndpoint() throws Exception{
//         Monitor monitor = new Monitor();
//         monitor.setName("monitor1");
//         monitor.setId(1L);

//         Monitor monitor2 = new Monitor();
//         monitor2.setName("monitor2");
//         monitor2.setId(2L);

//         MonitorGroup MonitorGroup = new MonitorGroup();
//         MonitorGroup.setName("group1");
//         MonitorGroup.setId(1L);
//         MonitorGroup.setDefaultGroup(false);
//         MonitorGroup.setMonitors(Arrays.asList(monitor, monitor2));

//         when(service.getGroupById(1L)).thenReturn(MonitorGroup);

//         mvc.perform(get("/api/groups/1/monitors")
//                         .contentType(MediaType.APPLICATION_JSON))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$", hasSize(2)))
//                 .andExpect(jsonPath("$[0].name", is("monitor1")))
//                 .andExpect(jsonPath("$[1].name", is("monitor2")));
//     }

//     @Test
//     void testCreateGroup() throws Exception{
//         Monitor monitor = new Monitor();
//         monitor.setName("monitor1");
//         monitor.setId(1L);

//         Monitor monitor2 = new Monitor();
//         monitor2.setName("monitor2");
//         monitor2.setId(2L);


//         MonitorGroup MonitorGroup = new MonitorGroup();
//         MonitorGroup.setName("group1");
//         MonitorGroup.setId(1L);
//         MonitorGroup.setDefaultGroup(false);
//         MonitorGroup.setMonitors(Arrays.asList(monitor, monitor2));

//         when(service.saveGroup(new MonitorGroup())).thenReturn(MonitorGroup);

//         mvc.perform(post("/api/groups")
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content(objectMapper.writeValueAsString(MonitorGroup)))
//                 .andExpect(status().isCreated())
//                 .andExpect(jsonPath("$.name", is("group1")))
//                 .andExpect(jsonPath("$.madeForMonitor", is(false)));
//     }

//     @Test
//     void testUpdateGroup() throws Exception{
//         Monitor monitor = new Monitor();
//         monitor.setName("monitor1");
//         monitor.setId(1L);

//         Monitor monitor2 = new Monitor();
//         monitor2.setName("monitor2");
//         monitor2.setId(2L);

//         MonitorGroup MonitorGroup = new MonitorGroup();
//         MonitorGroup.setName("group1");
//         MonitorGroup.setId(1L);
//         MonitorGroup.setDefaultGroup(false);
//         MonitorGroup.setMonitors(Arrays.asList(monitor, monitor2));

//         when(service.getGroupById(1L)).thenReturn(MonitorGroup);
//         when(service.updateGroup(Mockito.anyLong(),new MonitorGroup())).thenReturn(MonitorGroup);

//         mvc.perform(put("/api/groups/1")
//                         .contentType(MediaType.APPLICATION_JSON)
//                         .content(objectMapper.writeValueAsString(MonitorGroup)))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.name", is("group1")));
//     }

//     @Test
//     void testDeleteGroup() throws Exception{
//         Monitor monitor = new Monitor();
//         monitor.setName("monitor1");
//         monitor.setId(1L);

//         Monitor monitor2 = new Monitor();
//         monitor2.setName("monitor2");
//         monitor2.setId(2L);

//         MonitorGroup MonitorGroup = new MonitorGroup();
//         MonitorGroup.setName("group1");
//         MonitorGroup.setId(1L);
//         MonitorGroup.setDefaultGroup(false);
//         MonitorGroup.setMonitors(Arrays.asList(monitor, monitor2));

//         when(service.getGroupById(1L)).thenReturn(MonitorGroup);

//         mvc.perform(delete("/api/groups/1")
//                         .contentType(MediaType.APPLICATION_JSON))
//                 .andExpect(status().isNoContent());
//     }



// }



