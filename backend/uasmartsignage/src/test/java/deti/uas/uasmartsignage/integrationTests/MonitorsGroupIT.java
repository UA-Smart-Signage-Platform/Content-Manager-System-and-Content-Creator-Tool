package deti.uas.uasmartsignage.integrationTests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import deti.uas.uasmartsignage.Models.Monitor;
import deti.uas.uasmartsignage.Models.MonitorsGroup;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;


import java.util.List;

class MonitorsGroupIT extends BaseIntegrationTest{

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    void testGetAllMonitorsGroups() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<List<MonitorsGroup>> response = restTemplate.exchange("http://localhost:" + port + "/api/groups", HttpMethod.GET,
                entity, new ParameterizedTypeReference<List<MonitorsGroup>>() {});
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(3, response.getBody().size());
    }

    @Test
    void testGetMonitorsGroupsThatAreNotAssociatedWithOnlyOneMonitor() throws Exception{
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<List<MonitorsGroup>> response = restTemplate.exchange("http://localhost:" + port + "/api/groups/notMadeForMonitor", HttpMethod.GET,
                entity, new ParameterizedTypeReference<List<MonitorsGroup>>() {});
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("D23", response.getBody().get(0).getName());
    }

    @Test
    void testGetGroupByIdEndpoint(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<MonitorsGroup> response = restTemplate.exchange("http://localhost:" + port + "/api/groups/1", HttpMethod.GET,entity,MonitorsGroup.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getId());
        assertEquals("DETI", response.getBody().getName());
    }

}
