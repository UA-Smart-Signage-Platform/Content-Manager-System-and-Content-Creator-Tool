package pt.ua.deti.uasmartsignage.serviceTests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import pt.ua.deti.uasmartsignage.services.LogsService;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import pt.ua.deti.uasmartsignage.models.CustomFile;
import pt.ua.deti.uasmartsignage.models.FilesClass;
import pt.ua.deti.uasmartsignage.Repositories.FileRepository;
import pt.ua.deti.uasmartsignage.services.FileService;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {
    
    @Mock
    private FileRepository repository;

    @InjectMocks
    private FileService service;

    @Mock
    private LogsService logsService;

    @Test
    @Order(1)
    void whenGetFilesAtRoot_thenReturnFiles() {
        CustomFile customFile = new CustomFile("New directory", "directory", 0L, null);
        List<CustomFile> files = new ArrayList<>();
        files.add(customFile);

        when(repository.findAllByParentIsNull()).thenReturn(files);

        List<CustomFile> found = service.getFilesAtRoot();

        assertThat(found).isEqualTo(files);
        verify(repository, times(1)).findAllByParentIsNull();
    }

    @Test
    @Order(2)
    void whenGetFileById_thenReturnFile() {
        CustomFile customFile = new CustomFile("New directory", "directory", 1L, null);
        CustomFile saved = new CustomFile();
        when(repository.findById(1L)).thenReturn(Optional.of(customFile));

        Optional<CustomFile> found = service.getFileOrDirectoryById(1L);
        if (found.isPresent()) {
            saved = found.get();
        }

        assertThat(saved).isEqualTo(customFile);
        verify(repository, times(1)).findById(1L);
    }

    @Test
    @Order(3)
    void whenSaveFolder_thenFolderIsSaved() {
        CustomFile customFile = new CustomFile("New directory", "directory", 0L, null);

        when(repository.save(customFile)).thenReturn(customFile);

        CustomFile saved = service.createDirectory(customFile);

        assertThat(saved).isEqualTo(customFile);
        verify(repository, times(1)).save(customFile);

        String parentDirectoryPath = service.getParentDirectoryPath(customFile);
        String rootPath = System.getProperty("user.dir");
        File directory = new File(rootPath + parentDirectoryPath + customFile.getName());

        assertThat(directory).exists();

        // Clean up
        directory.delete();
    }

    @Test
    @Order(4)
    void whenUpdateFileName_thenFileNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        CustomFile updated = new CustomFile("Updated directory", "directory", 0L, null);
        updated.setId(1L);

        CustomFile saved = service.updateFileName(1L,updated);

        assertThat(saved).isNull();
        verify(repository, times(0)).save(updated);
    }

    @Test
    @Order(5)
    void whenSaveFolderInsideFolder_thenFolderIsSavedInsideFolder() {
        CustomFile outerFolder = new CustomFile("Outer directory", "directory", 0L, null);
        outerFolder.setId(1L);

        service.createDirectory(outerFolder);

        String parentDirectoryPathOuterF = service.getParentDirectoryPath(outerFolder);
        String rootPath = System.getProperty("user.dir");
        File outerDirectory = new File(rootPath + parentDirectoryPathOuterF + outerFolder.getName());

        assertThat(outerDirectory).exists();

        CustomFile innerFolder = new CustomFile("Inner directory", "directory", 100L, outerFolder);
        when(repository.save(innerFolder)).thenReturn(innerFolder);
        when(repository.findById(1L)).thenReturn(Optional.of(outerFolder));

        CustomFile savedInner = service.createDirectory(innerFolder);

        assertThat(savedInner).isEqualTo(innerFolder);
        verify(repository, times(1)).save(innerFolder);

        String parentDirectoryPathInnerF = service.getParentDirectoryPath(innerFolder);
        File innerDirectory = new File(rootPath + parentDirectoryPathInnerF + innerFolder.getName());

        assertThat(innerDirectory).exists().hasParent(outerDirectory);

        // Clean up
        if (innerDirectory.exists()) {
            innerDirectory.delete();
        }
        if (outerDirectory.exists()) {
            outerDirectory.delete();
        }
    }


    @Test
    @Order(6)
    void whenSaveFile_thenFileIsSaved() throws IOException {
        Path tempFile = Files.createTempFile("test", ".png");

        byte[] content = "Hello, World!".getBytes();
        Files.write(tempFile, content);

        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "file",                     // parameter name
                tempFile.getFileName().toString(), // file name
                "image/png",                // content type
                Files.readAllBytes(tempFile) // content as byte array
        );

        FilesClass filesClass = new FilesClass(null, mockMultipartFile);
        CustomFile saved = service.createFile(filesClass);

        File file = new File(System.getProperty("user.dir") + "/uploads/" + saved.getName());

        verify(repository, times(1)).save(any());
        assertThat(saved.getName()).isEqualTo(tempFile.getFileName().toString());

        //Clean up
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    @Order(7)
    void whenSaveFileInsideFolder_thenFileIsSavedInsideFolder() throws IOException {
        CustomFile outerFolder = new CustomFile("Outer directory", "directory", 0L, null);
        outerFolder.setId(1L);

        service.createDirectory(outerFolder);

        String parentDirectoryPathOuterF = service.getParentDirectoryPath(outerFolder);
        String rootPath = System.getProperty("user.dir");
        File outerDirectory = new File(rootPath + parentDirectoryPathOuterF + outerFolder.getName());

        assertThat(outerDirectory).exists();

        Path tempFile = Files.createTempFile("test", ".png");

        byte[] content = "Hello, World!".getBytes();
        Files.write(tempFile, content);

        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "file",                     // parameter name
                tempFile.getFileName().toString(), // file name
                "image/png",                // content type
                Files.readAllBytes(tempFile) // content as byte array
        );

        long fileSize = Files.size(tempFile);


        when(repository.findById(1L)).thenReturn(Optional.of(outerFolder));


        FilesClass filesClass = new FilesClass(outerFolder.getId(), mockMultipartFile);
        CustomFile savedInner = service.createFile(filesClass);


        String parentDirectoryPathInnerF = service.getParentDirectoryPath(savedInner);
        File innerDirectory = new File(rootPath + parentDirectoryPathInnerF + savedInner.getName());

        assertThat(innerDirectory).exists().hasParent(outerDirectory);
        //check if the parent size is updated
        assertThat(outerFolder.getSize()).isEqualTo(fileSize);
        verify(repository, times(3)).save(any());

        // Clean up
        if (innerDirectory.exists()) {
            innerDirectory.delete();
        }
        if (outerDirectory.exists()) {
            outerDirectory.delete();
        }
        Files.deleteIfExists(tempFile);
    }

    @Test
    @Order(8)
    void whenDeleteFile_thenFileIsDeleted() throws IOException {
        CustomFile customFile = new CustomFile("New directory", "directory", 0L, null);
        customFile.setId(1L);

        service.createDirectory(customFile);

        when(repository.findById(1L)).thenReturn(Optional.of(customFile));

        service.deleteFile(1L);

        String parentDirectoryPath = service.getParentDirectoryPath(customFile);
        String rootPath = System.getProperty("user.dir");
        File directory = new File(rootPath + parentDirectoryPath + customFile.getName());

        // Check if the file was deleted from the repository and from the disk
        assertThat(repository.findByName(customFile.getName())).isEmpty();
        assertThat(directory).doesNotExist();
        verify(repository, times(1)).delete(customFile);
    }

    @Test
    @Order(9)
    void whenDeleteFile_thenFileNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        boolean deleted = service.deleteFile(1L);

        assertThat(deleted).isFalse();
        verify(repository, times(0)).delete(any());
    }

    @Test
    @Order(10)
    void testDownloadFileById_FileExistsAndIsReadable() throws IOException {

        // Creating a temporary file
        Path tempFile = Files.createTempFile("test", ".png");
        byte[] content = "Hello, World!".getBytes();
        Files.write(tempFile, content);

        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "file",                     // parameter name
                tempFile.getFileName().toString(), // file name
                "image/png",                // content type
                Files.readAllBytes(tempFile) // content as byte array
        );

        FilesClass filesClass = new FilesClass(null, mockMultipartFile);
        CustomFile savedFile = service.createFile(filesClass);

        // Paths to the saved file and the root directory
        Path filePath = Paths.get(savedFile.getPath());
        String rootPath = System.getProperty("user.dir");
        String fileDirectory = rootPath + "/uploads/";

        when(repository.findById(1L)).thenReturn(Optional.of(savedFile));

        ResponseEntity<Resource> response = service.downloadFileById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(filePath.getFileName().toString(), response.getBody().getFilename());

        HttpHeaders headers = response.getHeaders();
        assertTrue(headers.containsKey(HttpHeaders.CONTENT_DISPOSITION));
        assertEquals("attachment; filename=\"" + filePath.getFileName().toString() + "\"",
               headers.getFirst(HttpHeaders.CONTENT_DISPOSITION));


        File file = new File(fileDirectory + savedFile.getName());
        assertTrue(file.exists());

        // Clean up
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    @Order(11)
    void testDownloadFileById_FileNotFound() throws MalformedURLException {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Resource> response = service.downloadFileById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

}
