package pt.ua.deti.uasmartsignage.controllerTests;

import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import pt.ua.deti.uasmartsignage.models.Content;
import pt.ua.deti.uasmartsignage.models.Template;
import pt.ua.deti.uasmartsignage.services.ContentService;
import pt.ua.deti.uasmartsignage.services.CustomUserDetailsService;
import pt.ua.deti.uasmartsignage.services.JwtUtilService;
import pt.ua.deti.uasmartsignage.services.WidgetService;
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

import pt.ua.deti.uasmartsignage.controllers.WidgetController;
import pt.ua.deti.uasmartsignage.models.Widget;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WidgetController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
class WidgetControllerTest {

        @Autowired
        private MockMvc mvc;

        @MockBean
        private WidgetService service;

        @MockBean
        private CustomUserDetailsService userDetailsService;

        @MockBean
        private JwtUtilService jwtUtil;

        @MockBean
        private ContentService contentService;

        private ObjectMapper objectMapper = new ObjectMapper();

        @Test
        void testGetAllWidgetsEndpoint() throws Exception{
            Content content = new Content();
            content.setName("content");
            content.setType("type");

            Widget widget1 = new Widget();
            widget1.setName("widget1");
            widget1.setPath("path");
            widget1.setContents(List.of(content));

            Widget widget2 = new Widget();
            widget2.setName("widget2");
            widget2.setPath("path");
            widget2.setContents(List.of(content));

            when(service.getAllWidgets()).thenReturn(Arrays.asList(widget1,widget2));

            mvc.perform(get("/api/widgets").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("widget1")))
                .andExpect(jsonPath("$[1].name", is("widget2")));
        }

        @Test
        void testGetWidgetByIdEndpoint() throws Exception{
            Content content = new Content();
            content.setName("content");
            content.setType("type");
    
            Template template = new Template();
            template.setName("template");

            Widget widget = new Widget();
            widget.setName("widget");
            widget.setPath("path");
            widget.setContents(List.of(content));

            when(service.getWidgetById(1L)).thenReturn(widget);

            mvc.perform(get("/api/widgets/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("widget")));
        }

        @Test
        void testSaveWidgetEndpoint() throws Exception{
            Content content = new Content();
            content.setName("content");
            content.setType("type");

            Template template = new Template();
            template.setName("template");

            Widget widget = new Widget();
            widget.setName("widget");
            widget.setPath("path");
            widget.setContents(List.of(content));

            when(service.saveWidget(Mockito.any(Widget.class))).thenReturn(widget);

            mvc.perform(post("/api/widgets").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(widget)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("widget")));
        }


        @Test
        void testDeleteWidgetEndpoint() throws Exception{
            Content content = new Content();
            content.setName("content");
            content.setType("type");

            Template template = new Template();
            template.setName("template");

            Widget widget = new Widget();
            widget.setName("widget");
            widget.setPath("path");
            widget.setContents(List.of(content));

            mvc.perform(delete("/api/widgets/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        }

        @Test
        void testUpdateWidgetEndpoint() throws Exception{
            Widget widget = new Widget();
            widget.setName("updated_widget");
            widget.setPath("new_path");

            when(service.updateWidget(Mockito.anyLong(), Mockito.any())).thenReturn(widget);

            mvc.perform(put("/api/widgets/1").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(widget)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("updated_widget")))
                .andExpect(jsonPath("$.path", is("new_path")));
        }

        @Test
        void testGetWidgetByIdEndpoint404() throws Exception{
            when(service.getWidgetById(1L)).thenReturn(null);

            mvc.perform(get("/api/widgets/1").contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }

        @Test
        void testUpdateWidgetEndpoint404() throws Exception{
            Widget widget = new Widget();
            widget.setName("widget");
            widget.setPath("path");

            when(service.updateWidget(Mockito.anyLong(), Mockito.any())).thenReturn(null);

            mvc.perform(put("/api/widgets/10000").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(widget)))
                .andExpect(status().isNotFound());
        }
}
