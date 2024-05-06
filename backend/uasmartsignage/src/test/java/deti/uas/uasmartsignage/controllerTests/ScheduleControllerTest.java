package deti.uas.uasmartsignage.controllerTests;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

import deti.uas.uasmartsignage.Models.MonitorsGroup;
import deti.uas.uasmartsignage.Models.AppUser;
import deti.uas.uasmartsignage.Services.CustomUserDetailsService;
import deti.uas.uasmartsignage.Services.ScheduleService;
import deti.uas.uasmartsignage.Services.jwtUtilService;
import deti.uas.uasmartsignage.authentication.IAuthenticationFacade;

import org.hibernate.validator.constraints.time.DurationMax;
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

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import deti.uas.uasmartsignage.Controllers.ScheduleController;
import deti.uas.uasmartsignage.Models.Schedule;
import deti.uas.uasmartsignage.Services.ScheduleService;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ScheduleController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
class ScheduleControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private ScheduleService service;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private deti.uas.uasmartsignage.Services.jwtUtilService jwtUtil;

    @MockBean
    private IAuthenticationFacade authenticationFacade;


    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGetAllSchedulesEndpoint() throws Exception{
        AppUser user = new AppUser();
        user.setEmail("admin");
        user.setRole("ADMIN");

        MonitorsGroup group = new MonitorsGroup();
        group.setName("group1");

        Schedule schedule1 = new Schedule();
        schedule1.setFrequency(10);
        schedule1.setCreatedBy(user);
        schedule1.setEndDate(LocalDate.parse("2024-04-21"));
        schedule1.setStartDate(LocalDate.parse("2024-04-21"));
        schedule1.setPriority(1);
        schedule1.setLastEditedBy(user);
        schedule1.setMonitorsGroupForSchedules(group);

        Schedule schedule2 = new Schedule();
        schedule2.setFrequency(5);
        schedule2.setCreatedBy(user);
        schedule2.setEndDate(LocalDate.parse("2024-04-21"));
        schedule2.setStartDate(LocalDate.parse("2024-04-21"));
        schedule2.setPriority(1);
        schedule2.setLastEditedBy(user);
        schedule2.setMonitorsGroupForSchedules(group);

        when(service.getAllSchedules()).thenReturn(Arrays.asList(schedule1,schedule2));

        mvc.perform(get("/api/schedules").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].frequency", is(10)))
            .andExpect(jsonPath("$[1].frequency", is(5)));
    }

    @Test
    void testGetScheduleByIdEndpoint() throws Exception{
        AppUser user = new AppUser();
        user.setEmail("admin");
        user.setRole("ADMIN");

        MonitorsGroup group = new MonitorsGroup();
        group.setName("group1");

        Schedule schedule = new Schedule();
        schedule.setFrequency(3);
        schedule.setCreatedBy(user);
        schedule.setEndDate(LocalDate.parse("2024-04-21"));
        schedule.setStartDate(LocalDate.parse("2024-04-21"));
        schedule.setPriority(1);
        schedule.setLastEditedBy(user);
        schedule.setMonitorsGroupForSchedules(group);

        when(service.getScheduleById(1L)).thenReturn(schedule);

        mvc.perform(get("/api/schedules/1").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.frequency", is(3)))
            .andExpect(jsonPath("$.priority", is(1)));
    }

    @Test
    void testSaveScheduleEndpoint() throws Exception{
        AppUser user = new AppUser();
        user.setEmail("admin");
        user.setRole("ADMIN");

        MonitorsGroup group = new MonitorsGroup();
        group.setName("group1");

        Schedule schedule = new Schedule();
        schedule.setFrequency(1);
        schedule.setCreatedBy(user);
        schedule.setPriority(3);
        schedule.setLastEditedBy(user);
        schedule.setMonitorsGroupForSchedules(group);

        when(service.saveSchedule(Mockito.any())).thenReturn(schedule);

        mvc.perform(post("/api/schedules").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(schedule)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.frequency", is(1)))
            .andExpect(jsonPath("$.priority", is(3)));
    }

    @Test
    void testDeleteScheduleEndpoint() throws Exception{
        AppUser user = new AppUser();
        user.setEmail("admin");
        user.setRole("ADMIN");

        MonitorsGroup group = new MonitorsGroup();
        group.setName("group1");

        Schedule schedule = new Schedule();
        schedule.setFrequency(1);
        schedule.setCreatedBy(user);
        schedule.setEndDate(LocalDate.parse("2024-04-21"));
        schedule.setStartDate(LocalDate.parse("2024-04-21"));
        schedule.setPriority(1);
        schedule.setLastEditedBy(user);
        schedule.setMonitorsGroupForSchedules(group);

        when(service.getScheduleById(1L)).thenReturn(schedule);

        mvc.perform(delete("/api/schedules/1").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    //missing update test


}
