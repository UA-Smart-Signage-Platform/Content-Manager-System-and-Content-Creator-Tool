package pt.ua.deti.uasmartsignage.controllerTests;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import pt.ua.deti.uasmartsignage.controllers.FileController;
import pt.ua.deti.uasmartsignage.enums.Log;
import pt.ua.deti.uasmartsignage.models.CustomFile;
import pt.ua.deti.uasmartsignage.services.CustomUserDetailsService;
import pt.ua.deti.uasmartsignage.services.FileService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;


import com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import pt.ua.deti.uasmartsignage.services.MonitorService;
import pt.ua.deti.uasmartsignage.services.JwtUtilService;
import pt.ua.deti.uasmartsignage.authentication.IAuthenticationFacade;

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
    private JwtUtilService jwtUtil;

    @MockBean
    private IAuthenticationFacade authenticationFacade;

    @MockBean
    private FileService fileService;

    @MockBean
    private MonitorService monitorService;

    private ObjectMapper objectMapper = new ObjectMapper();

    CustomFile customFile = new CustomFile();
    CustomFile customFile2 = new CustomFile();
    CustomFile customFile3 = new CustomFile();

    @BeforeEach
    void setUp(){
        customFile = new CustomFile("New directory", UUID.randomUUID().toString(), "directory", "", 0L, null);
        customFile2 = new CustomFile("Old directory", UUID.randomUUID().toString(), "file", "png", 0L, customFile);
        customFile3 = new CustomFile("Inner directory", UUID.randomUUID().toString(), "directory", "", 0L, customFile);
    }

    @Test
    void givenValidId_whenGetFileById_thenReturnFile() throws Exception {
        when(fileService.getFileById(anyLong())).thenReturn(Optional.of(customFile));

        mvc.perform(get("/api/files/1").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is("New directory")));

        verify(fileService, Mockito.times(1)).getFileById(anyLong());
    }

    @Test
    void givenInvalidId_whenGetFileById_thenReturnNotFound() throws Exception {
        when(fileService.getFileById(1L)).thenReturn(Optional.empty());

        mvc.perform(get("/api/files/1").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());

        verify(fileService, Mockito.times(1)).getFileById(1L);
    }

    @Test
    void givenMultipleFiles_whenGetRootFiles_thenReturnList() throws Exception {
        when(fileService.getFilesAtRoot()).thenReturn(Arrays.asList(customFile));

        mvc.perform(get("/api/files/directory/root").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].name", is("New directory")));

        verify(fileService, Mockito.times(1)).getFilesAtRoot();
    }

    @Test
    void givenValidFile_whenCreateFile_thenReturnFile() throws Exception {
        when(fileService.createFile(Mockito.any())).thenReturn(customFile2);

        mvc.perform(post("/api/files").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(customFile2)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name", is("Old directory")));

        verify(fileService, Mockito.times(1)).createFile(Mockito.any());
    }

    @Test
    void givenInvalidFile_whenCreateFile_thenReturnBadRequest() throws Exception {
        when(fileService.createFile(Mockito.any())).thenReturn(null);

        mvc.perform(post("/api/files").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(customFile)))
            .andExpect(status().isBadRequest());

        verify(fileService, Mockito.times(1)).createFile(Mockito.any());
    }


    @Test
    void givenValidFile_whenCreateDirectory_thenReturnFile() throws Exception {
        when(fileService.createDirectory(Mockito.any())).thenReturn(customFile);

        mvc.perform(post("/api/files/directory").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(customFile)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name", is("New directory")))
            .andExpect(jsonPath("$.type", is("directory")));;
    }

    @Test
    void givenInvalidFile_whenCreateDirectory_thenReturnBadRequest() throws Exception {
        when(fileService.createDirectory(Mockito.any())).thenReturn(null);

        mvc.perform(post("/api/files/directory").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(customFile)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void givenValidId_whenDeleteFile_thenReturnTrue() throws Exception {
        when(fileService.deleteFile(1L)).thenReturn(true);

        mvc.perform(delete("/api/files/1").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andExpect(jsonPath("$", is(true)));

        verify(fileService, Mockito.times(1)).deleteFile(1L);
    }

    @Test
    void givenInvalidId_whenDeleteFile_thenReturnNotFound() throws Exception {
        when(fileService.deleteFile(1L)).thenReturn(false);

        mvc.perform(delete("/api/files/1").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());

        verify(fileService, Mockito.times(1)).deleteFile(1L);
    }

    // TODO MISSING DOWNLOAD LOGIC
    @Test
    @Disabled
    void givenValidId_whenDownloadFile_thenReturnResource() throws Exception {
        Path filePath = Paths.get(Log.USERDIR.toString() + "/uploads/" + customFile2.getUuid() + "." + customFile2.getExtension());
        Resource fileResource = new UrlResource(filePath.toUri());
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileResource.getFilename() + "\"");
        when(fileService.downloadFileById(1L)).thenReturn(ResponseEntity.ok().headers(headers).body(fileResource));

        mvc.perform(get("/api/files/download/1").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        verify(fileService, Mockito.times(1)).downloadFileById(1L);
    }

    @Test
    void givenInvalidId_whenDownloadFile_thenReturnNotFound() throws Exception {
        when(fileService.downloadFileById(1L)).thenReturn(null);

        mvc.perform(get("/api/files/download/1").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());

        verify(fileService, Mockito.times(1)).downloadFileById(1L);
    }

    @Test
    void givenValidId_whenUpdateFile_thenReturnFile() throws Exception {
        when(fileService.updateFileName(1L, "newName")).thenReturn(customFile);

        mvc.perform(put("/api/files/1").contentType(MediaType.APPLICATION_JSON)
            .content("newName"))
            .andExpect(status().isOk());

        verify(fileService, Mockito.times(1)).updateFileName(1L, "newName");
    }

    @Test
    void givenInvalidId_whenUpdateFile_thenReturnNotFound() throws Exception {
        when(fileService.updateFileName(1L, "newName")).thenReturn(null);

        mvc.perform(put("/api/files/1").contentType(MediaType.APPLICATION_JSON)
            .content("newName"))
            .andExpect(status().isNotFound());

        verify(fileService, Mockito.times(1)).updateFileName(1L, "newName");
    }
}
