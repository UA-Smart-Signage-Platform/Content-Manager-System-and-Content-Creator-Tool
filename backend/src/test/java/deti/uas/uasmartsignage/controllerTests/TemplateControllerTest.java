package pt.ua.deti.uasmartsignage.controllerTests;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import pt.ua.deti.uasmartsignage.Models.Template;
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

import pt.ua.deti.uasmartsignage.Controllers.TemplateController;
import pt.ua.deti.uasmartsignage.Services.TemplateService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TemplateController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
class TemplateControllerTest {

            @Autowired
            private MockMvc mvc;

            @MockBean
            private TemplateService service;

            @MockBean
            private CustomUserDetailsService userDetailsService;

            @MockBean
            private JwtUtilService jwtUtil;

            private ObjectMapper objectMapper = new ObjectMapper();

            @Test
            void testGetAllTemplatesEndpoint() throws Exception{
                Template template1 = new Template();
                template1.setName("template1");

                Template template2 = new Template();
                template2.setName("template2");

                when(service.getAllTemplates()).thenReturn(Arrays.asList(template1, template2));

                mvc.perform(get("/api/templates")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$", hasSize(2)))
                        .andExpect(jsonPath("$[0].name", is(template1.getName())))
                        .andExpect(jsonPath("$[1].name", is(template2.getName())));
            }

            @Test
            void testGetTemplateByIdEndpoint() throws Exception{
                Template template = new Template();
                template.setName("template");
                when(service.getTemplateById(1L)).thenReturn(template);

                mvc.perform(get("/api/templates/1")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.name", is(template.getName())));
            }

            @Test
            void testCreateTemplateEndpoint() throws Exception{
                Template template = new Template();
                template.setName("template");
                when(service.saveTemplate(Mockito.any(Template.class))).thenReturn(template);

                mvc.perform(post("/api/templates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(template)))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.name", is(template.getName())));
            }

            @Test
            void testUpdateTemplateEndpoint() throws Exception{
                Template template = new Template();
                template.setName("updated_template");
                when(service.updateTemplate(Mockito.anyLong(),Mockito.any())).thenReturn(template);

                mvc.perform(put("/api/templates/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(template)))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.name", is("updated_template")));
            }

            @Test
            void testDeleteTemplateEndpoint() throws Exception{
                mvc.perform(delete("/api/templates/1")
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNoContent());

                verify(service).deleteTemplate(1L);//checks if deleteTemplate is called with id 1L
            }

            @Test
            void testGetTemplateByIdEndpoint404() throws Exception{
                when(service.getTemplateById(1L)).thenReturn(null);

                mvc.perform(get("/api/templates/1").contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
            }

            @Test
            void testUpdateTemplateEndpoint404() throws Exception{
                Template template = new Template();
                template.setName("updated_template");
                when(service.updateTemplate(Mockito.anyLong(), Mockito.any())).thenReturn(null);

                mvc.perform(put("/api/templates/1000").contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(template)))
                        .andExpect(status().isNotFound());
            }


}
