package deti.uas.uasmartsignage.controllerTests;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


import deti.uas.uasmartsignage.Controllers.TemplateGroupController;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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

import deti.uas.uasmartsignage.Models.TemplateWidget;
import deti.uas.uasmartsignage.Models.Widget;
import deti.uas.uasmartsignage.Repositories.TemplateWidgetRepository;
import deti.uas.uasmartsignage.Services.TemplateWidgetService;
import deti.uas.uasmartsignage.Controllers.TemplateWidgetController;
import deti.uas.uasmartsignage.Models.Template;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TemplateWidgetController.class)
@ActiveProfiles("test")
class TemplateWidgetControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private TemplateWidgetService service;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGetAllTemplateWidgets() throws Exception {
        Template template1 = new Template();
        template1.setName("template1");

        Widget widget1 = new Widget();
        widget1.setName("widget1");

        TemplateWidget templateWidget1 = new TemplateWidget();
        templateWidget1.setTemplate(template1);
        templateWidget1.setWidget(widget1);
        templateWidget1.setTop(1);
        templateWidget1.setLeftPosition(1);
        templateWidget1.setWidth(1);
        templateWidget1.setHeight(1);

        Template template2 = new Template();
        template2.setName("template2");

        Widget widget2 = new Widget();
        widget2.setName("widget2");

        TemplateWidget templateWidget2 = new TemplateWidget();
        templateWidget2.setTemplate(template2);
        templateWidget2.setWidget(widget2);
        templateWidget2.setTop(2);
        templateWidget2.setLeftPosition(2);
        templateWidget2.setWidth(2);
        templateWidget2.setHeight(2);

        List<TemplateWidget> templateWidgets = Arrays.asList(templateWidget1, templateWidget2);

        when(service.getAllTemplateWidgets()).thenReturn(templateWidgets);

        mvc.perform(get("/templateWidgets")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].template.name", is(template1.getName())))
                .andExpect(jsonPath("$[1].template.name", is(template2.getName())));
    }

    @Test
    void testGetTemplateWigetById() throws Exception {
        Template template1 = new Template();
        template1.setName("template1");

        Widget widget1 = new Widget();
        widget1.setName("widget1");

        TemplateWidget templateWidget1 = new TemplateWidget();
        templateWidget1.setTemplate(template1);
        templateWidget1.setWidget(widget1);
        templateWidget1.setTop(1);
        templateWidget1.setLeftPosition(1);
        templateWidget1.setWidth(1);
        templateWidget1.setHeight(1);

        when(service.getTemplateWidgetById(1L)).thenReturn(templateWidget1);

        mvc.perform(get("/templateWidgets/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.template.name", is(template1.getName())))
                .andExpect(jsonPath("$.top", is(1)));
    }

    @Test
    void testGetTemplateWidgetById404() throws Exception{
        mvc.perform(get("/templateWidgets/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testSaveTemplateWidget() throws Exception {
        Template template1 = new Template();
        template1.setName("template1");

        Widget widget1 = new Widget();
        widget1.setName("widget1");

        TemplateWidget templateWidget1 = new TemplateWidget();
        templateWidget1.setTemplate(template1);
        templateWidget1.setWidget(widget1);
        templateWidget1.setTop(1);
        templateWidget1.setLeftPosition(1);
        templateWidget1.setWidth(10);
        templateWidget1.setHeight(1);

        when(service.saveTemplateWidget(Mockito.any(TemplateWidget.class))).thenReturn(templateWidget1);

        mvc.perform(post("/templateWidgets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(templateWidget1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.template.name", is(template1.getName())))
                .andExpect(jsonPath("$.width", is(10)));
    }

    @Test
    void testDeleteTemplateWidget() throws Exception {
        mvc.perform(delete("/templateWidgets/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void testUpdateTemplateWidget() throws Exception {
        Template template1 = new Template();
        template1.setName("template1");

        Template template2 = new Template();
        template2.setName("template2");

        Widget widget1 = new Widget();
        widget1.setName("widget1");

        TemplateWidget templateWidget1 = new TemplateWidget();
        templateWidget1.setTemplate(template1);
        templateWidget1.setWidget(widget1);
        templateWidget1.setTop(1);
        templateWidget1.setLeftPosition(1);
        templateWidget1.setWidth(1);
        templateWidget1.setHeight(1);


        TemplateWidget templateWidget2 = new TemplateWidget();
        templateWidget2.setTemplate(template2);
        templateWidget2.setWidget(widget1);
        templateWidget2.setTop(2);
        templateWidget2.setLeftPosition(2);
        templateWidget2.setWidth(2);
        templateWidget2.setHeight(2);

        when(service.updateTemplateWidget(Mockito.anyLong(), Mockito.any(TemplateWidget.class))).thenReturn(templateWidget2);

        mvc.perform(put("/templateWidgets/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(templateWidget1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.template.name", is(template2.getName())))
                .andExpect(jsonPath("$.top", is(2)));
    }

    @Test
    void testUpdateWidget404() throws Exception{
        Template template1 = new Template();
        template1.setName("template1");

        Template template2 = new Template();
        template2.setName("template2");

        Widget widget1 = new Widget();
        widget1.setName("widget1");

        TemplateWidget templateWidget1 = new TemplateWidget();
        templateWidget1.setTemplate(template1);
        templateWidget1.setWidget(widget1);
        templateWidget1.setTop(1);
        templateWidget1.setLeftPosition(1);
        templateWidget1.setWidth(1);
        templateWidget1.setHeight(1);


        TemplateWidget templateWidget2 = new TemplateWidget();
        templateWidget2.setTemplate(template2);
        templateWidget2.setWidget(widget1);
        templateWidget2.setTop(2);
        templateWidget2.setLeftPosition(2);
        templateWidget2.setWidth(2);
        templateWidget2.setHeight(2);

        when(service.updateTemplateWidget(Mockito.anyLong(), Mockito.any(TemplateWidget.class))).thenReturn(null);

        mvc.perform(put("/templateWidgets/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(templateWidget1)))
                .andExpect(status().isNotFound());

    }


}
