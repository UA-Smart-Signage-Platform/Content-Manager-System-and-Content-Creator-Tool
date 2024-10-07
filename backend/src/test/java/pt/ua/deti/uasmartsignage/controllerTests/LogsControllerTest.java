package pt.ua.deti.uasmartsignage.controllerTests;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.http.MediaType;
import pt.ua.deti.uasmartsignage.authentication.IAuthenticationFacade;
import pt.ua.deti.uasmartsignage.controllers.LogController;
import pt.ua.deti.uasmartsignage.enums.Severity;
import pt.ua.deti.uasmartsignage.models.embedded.BackendLog;
import pt.ua.deti.uasmartsignage.services.CustomUserDetailsService;
import pt.ua.deti.uasmartsignage.services.JwtUtilService;
import pt.ua.deti.uasmartsignage.services.LogsService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LogController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
class LogsControllerTest {
    
    @Autowired
    private MockMvc mvc;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private JwtUtilService jwtUtil;

    @MockBean
    private IAuthenticationFacade authenticationFacade;

    @MockBean
    private LogsService service;

    BackendLog backendLog = new BackendLog();
    BackendLog backendLog2 = new BackendLog();
    BackendLog backendLog3 = new BackendLog();

    @Test
    void givenVariousBackendlogs_whenGetBackendLogs_thenReturnList() throws Exception {
        when(service.getBackendLogs(10)).thenReturn(Arrays.asList(backendLog, backendLog2, backendLog3));

        mvc.perform(get("/api/logs/backend?hours=10").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(3)));

        verify(service, Mockito.times(1)).getBackendLogs(10);
    }

    @Test
    void givenVariousBackendlogs_whenGetBackendLogsByNumberDaysAndSeverity_thenReturnListWithNumberLogsPerDay() throws Exception {
        when(service.getBackendLogsByNumberDaysAndSeverity(10, Severity.INFO)).thenReturn(Arrays.asList(942, 30, 22));

        mvc.perform(get("/api/logs/backend/count?days=10&severity=INFO").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(3)));

        verify(service, Mockito.times(1)).getBackendLogsByNumberDaysAndSeverity(10, Severity.INFO);
    }

    @Test
    void givenVariousBackendlogs_whenGetBackendLogsNumberPerOperationByNumberDaysAndSeverity_thenReturnNumberOfOccurencesPerOperationPerDay() throws Exception {
        Map<String, Integer> countPerOperationPerDay = new HashMap<String, Integer>();
        countPerOperationPerDay.put("Some operation name", 10);
        countPerOperationPerDay.put("A differnt operation name", 30);
        countPerOperationPerDay.put("Yet another one", 20);
        
        when(service.getBackendLogsNumberPerOperationByNumberDaysAndSeverity(10, Severity.INFO)).thenReturn(countPerOperationPerDay);

        mvc.perform(get("/api/logs/backend/operation?days=10&severity=INFO").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.['Some operation name']").value(10))
            .andExpect(jsonPath("$.['A differnt operation name']").value(30))
            .andExpect(jsonPath("$.['Yet another one']").value(20));

        verify(service, Mockito.times(1)).getBackendLogsNumberPerOperationByNumberDaysAndSeverity(10, Severity.INFO);
    }
}
