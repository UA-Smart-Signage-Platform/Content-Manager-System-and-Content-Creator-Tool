package deti.uas.uasmartsignage.controllerTests;


import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;

import deti.uas.uasmartsignage.Controllers.FileController;
import deti.uas.uasmartsignage.Models.CustomFile;
import deti.uas.uasmartsignage.Services.CustomUserDetailsService;
import deti.uas.uasmartsignage.Services.FileService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;


import com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import deti.uas.uasmartsignage.Controllers.MonitorController;
import deti.uas.uasmartsignage.Models.Monitor;
import deti.uas.uasmartsignage.Services.MonitorService;
import deti.uas.uasmartsignage.Services.jwtUtilService;
import deti.uas.uasmartsignage.authentication.IAuthenticationFacade;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@WebMvcTest(FileController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
class FileControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private jwtUtilService jwtUtil;

    @MockBean
    private IAuthenticationFacade authenticationFacade;

    @MockBean
    private FileService fileService;

    @MockBean
    private MonitorService monitorService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGetFileByIdEndpoint() throws Exception {
        CustomFile file = new CustomFile();
        file.setId(1L);
        file.setName("testFile");

        when(fileService.getFileOrDirectoryById(1L)).thenReturn(Optional.of(file));

        mvc.perform(get("/api/files/1").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is("testFile")));

        verify(fileService, Mockito.times(1)).getFileOrDirectoryById(1L);
    }

    @Test
    void testGetFileByIdEndpoint404() throws Exception {
        when(fileService.getFileOrDirectoryById(1L)).thenReturn(Optional.empty());

        mvc.perform(get("/api/files/1").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());

        verify(fileService, Mockito.times(1)).getFileOrDirectoryById(1L);
    }

    @Test
    void testGetFilesAtRootEndpoint() throws Exception {
        CustomFile file = new CustomFile();
        file.setId(1L);
        file.setName("testFile");

        when(fileService.getFilesAtRoot()).thenReturn(Arrays.asList(file));

        mvc.perform(get("/api/files/directory/root").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].name", is("testFile")));

        verify(fileService, Mockito.times(1)).getFilesAtRoot();
    }

    @Test
    void testCreateFileEndpoint() throws Exception {
        CustomFile file = new CustomFile();
        file.setId(1L);
        file.setName("testFile");

        when(fileService.createFile(Mockito.any())).thenReturn(file);

        mvc.perform(post("/api/files").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(file)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name", is("testFile")));

        verify(fileService, Mockito.times(1)).createFile(Mockito.any());
    }

    @Test
    void testCreateFileEndpoint400() throws Exception {
        CustomFile file = new CustomFile();
        file.setId(1L);
        file.setName("testFile");

        when(fileService.createFile(Mockito.any())).thenReturn(null);

        mvc.perform(post("/api/files").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(file)))
            .andExpect(status().isBadRequest());

        verify(fileService, Mockito.times(1)).createFile(Mockito.any());
    }


    @Test
    void testCreateDirectoryEndpoint() throws Exception {
        CustomFile file = new CustomFile();
        file.setId(1L);
        file.setName("testFile");

        when(fileService.createDirectory(Mockito.any())).thenReturn(file);

        mvc.perform(post("/api/files/directory").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(file)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name", is("testFile")));
    }

    @Test
    void testCreateDirectoryEndpoint400() throws Exception {
        CustomFile file = new CustomFile();
        file.setId(1L);
        file.setName("testFile");

        when(fileService.createDirectory(Mockito.any())).thenReturn(null);

        mvc.perform(post("/api/files/directory").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(file)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteFileEndpoint() throws Exception {
        CustomFile file = new CustomFile();
        file.setId(1L);
        file.setName("testFile");

        when(fileService.deleteFile(1L)).thenReturn(true);

        mvc.perform(delete("/api/files/1").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        verify(fileService, Mockito.times(1)).deleteFile(1L);
    }

    @Test
    void testDeleteFileEndpoint404() throws Exception {
        CustomFile file = new CustomFile();
        file.setId(1L);
        file.setName("testFile");

        when(fileService.deleteFile(1L)).thenReturn(false);

        mvc.perform(delete("/api/files/1").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());

        verify(fileService, Mockito.times(1)).deleteFile(1L);
    }

    @Test
    void testDownloadFileEndpoint() throws Exception {
        CustomFile file = new CustomFile();
        file.setId(1L);
        file.setName("testFile");

        when(fileService.downloadFileById(1L)).thenReturn(ResponseEntity.notFound().build());

        mvc.perform(get("/api/files/download/1").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());

        verify(fileService, Mockito.times(1)).downloadFileById(1L);
    }

}
