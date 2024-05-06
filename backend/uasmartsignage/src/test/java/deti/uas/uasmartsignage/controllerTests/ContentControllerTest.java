package deti.uas.uasmartsignage.controllerTests;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;

import deti.uas.uasmartsignage.Services.ContentService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import deti.uas.uasmartsignage.Controllers.ContentController;
import deti.uas.uasmartsignage.Models.Content;
import deti.uas.uasmartsignage.Models.Widget;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ContentController.class)
@ActiveProfiles("test")
class ContentControllerTest {

        @Autowired
        private MockMvc mvc;

        @MockBean
        private ContentService service;

        private ObjectMapper objectMapper = new ObjectMapper();


        @Test
        void testGetAllContentsEndpoint() throws Exception{
            Widget widget = new Widget();
            widget.setName("Widget1");

            Content content = new Content();
            content.setName("Content1");
            content.setType("image");
            content.setWidget(widget);
            content.setOptions(new ArrayList<>());

            Content content2 = new Content();
            content2.setName("Content2");
            content2.setType("video");
            content2.setWidget(widget);
            content2.setOptions(new ArrayList<>());

            when(service.getAllContents()).thenReturn(Arrays.asList(content,content2));

            mvc.perform(get("/content").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Content1")))
                .andExpect(jsonPath("$[1].name", is("Content2")));
        }

        @Test
        void testGetContentByIdEndpoint() throws Exception{
            Widget widget = new Widget();
            widget.setName("Widget");

            Content content = new Content();
            content.setName("Content");
            content.setType("image");
            content.setWidget(widget);
            content.setOptions(new ArrayList<>());

            when(service.getContentById(1L)).thenReturn(content);

            mvc.perform(get("/content/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Content")))
                .andExpect(jsonPath("$.type", is("image")));
        }


        @Test
        void testSaveContentEndpoint() throws Exception{
            Widget widget = new Widget();
            widget.setName("Widget");

            Content content = new Content();
            content.setName("Content");
            content.setType("image");
            content.setWidget(widget);
            content.setOptions(new ArrayList<>());

            when(service.saveContent(Mockito.any())).thenReturn(content);

            mvc.perform(post("/content").contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(content)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.name", is("Content")))
                    .andExpect(jsonPath("$.type", is("image")));
        }


        @Test
        void testDeleteContentEndpoint() throws Exception{
            Widget widget = new Widget();
            widget.setName("New Widget");

            Content content = new Content();
            content.setName("New Content");
            content.setType("type");
            content.setWidget(widget);
            content.setOptions(new ArrayList<>());

            when(service.getContentById(1L)).thenReturn(content);

            mvc.perform(delete("/content/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        }

        @Test
        void testUpdateContentEndpoint() throws Exception{
            Widget widget = new Widget();
            widget.setName("Widget");

            Content content = new Content();
            content.setName("updated_content");
            content.setType("updated_image");
            content.setWidget(widget);
            content.setOptions(new ArrayList<>());

            when(service.updateContent(Mockito.anyLong(), Mockito.any())).thenReturn(content);

            mvc.perform(put("/content/1").contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(content)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name", is("updated_content")))
                    .andExpect(jsonPath("$.type", is("updated_image")));
        }

        @Test
        void testGetContentByIdEndpoint404() throws Exception{
            when(service.getContentById(1L)).thenReturn(null);

            mvc.perform(get("/content/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        }

        @Test
        void testUpdateContentEndpoint404() throws Exception{
            Widget widget = new Widget();
            widget.setName("Widget");

            Content content = new Content();
            content.setName("Content");
            content.setType("image");
            content.setWidget(widget);
            content.setOptions(new ArrayList<>());

            when(service.updateContent(Mockito.anyLong(), Mockito.any())).thenReturn(null);

            mvc.perform(put("/content/1000").contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(content)))
                    .andExpect(status().isNotFound());
        }


}
