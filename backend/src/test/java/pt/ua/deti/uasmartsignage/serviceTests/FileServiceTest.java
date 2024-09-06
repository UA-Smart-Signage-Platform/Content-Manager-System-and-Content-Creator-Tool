package pt.ua.deti.uasmartsignage.serviceTests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import pt.ua.deti.uasmartsignage.services.LogsService;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;

import pt.ua.deti.uasmartsignage.enums.Log;
import pt.ua.deti.uasmartsignage.models.CustomFile;
import pt.ua.deti.uasmartsignage.models.embedded.FilesClass;
import pt.ua.deti.uasmartsignage.repositories.FileRepository;
import pt.ua.deti.uasmartsignage.services.FileService;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {
    
    @Mock
    private FileRepository repository;

    @InjectMocks
    private FileService service;

    @Mock
    private LogsService logsService;

    CustomFile customFile = new CustomFile();
    CustomFile customFile2 = new CustomFile();
    CustomFile customFile3 = new CustomFile();

    @BeforeAll
    static void preSetUp(){
        File directory = new File(Log.USERDIR.toString() + "/uploads/");
        if (!directory.exists()){
            directory.mkdir();
        }
    }

    @BeforeEach
    void setUp(){
        customFile = new CustomFile("New directory", UUID.randomUUID().toString(), "directory", "", 0L, null);
        customFile2 = new CustomFile("Old directory", UUID.randomUUID().toString(), "file", "png", 0L, customFile);
        customFile3 = new CustomFile("Inner directory", UUID.randomUUID().toString(), "directory", "", 0L, customFile);
    }

    @Test
    void givenFiles_whenGetFilesAtRoot_thenReturnFilesAtRoot() {
        when(repository.findAllByParentIsNull()).thenReturn(Arrays.asList(customFile));

        List<CustomFile> files = service.getFilesAtRoot();

        assertThat(files).hasSize(1).containsOnly(customFile);
        verify(repository, times(1)).findAllByParentIsNull();
    }

    @Test
    void givenId_whenGetFileById_thenReturnFile() {
        when(repository.findById(1L)).thenReturn(Optional.of(customFile));

        Optional<CustomFile> foundFile = service.getFileById(1L);

        assertThat(foundFile).isPresent().contains(customFile);
        verify(repository, times(1)).findById(1L);        
    }

    @Test
    void givenInvalidId_whenGetFileById_thenReturnEmpty() {
        when(repository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<CustomFile> foundFile = service.getFileById(10L);

        assertThat(foundFile).isEmpty();
    }

    @Test
    void givenValidType_whenCreateDirectory_thenDirectoryIsSaved() {
        // Repository test
        when(repository.save(customFile)).thenReturn(customFile);

        CustomFile saved = service.createDirectory(customFile);

        assertThat(saved).isEqualTo(customFile);
        verify(repository, times(1)).save(customFile);

        // Disk test
        File directory = new File(System.getProperty("user.dir") + "/uploads/" + customFile.getUuid());

        assertThat(directory).exists();

        directory.delete();
    }

    @Test
    void givenInvalidType_whenCreateDirectory_thenReturnNull() {
        customFile.setType("Not a directory");

        CustomFile saved = service.createDirectory(customFile);

        assertThat(saved).isNull();
        verify(repository, never()).save(any(CustomFile.class));
    }

    @Test
    void givenValidFile_whenCreateFile_thenFileIsSaved() throws IOException {
        Path tempFile = Files.createTempFile("test", ".png");

        byte[] content = "Hello, World!".getBytes();
        Files.write(tempFile, content);

        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "file",                             // parameter name
                tempFile.getFileName().toString(),      // file name
                "image/png",                // content type
                Files.readAllBytes(tempFile)            // content as byte array
        );

        // Repository test
        FilesClass filesClass = new FilesClass(null, mockMultipartFile);
        CustomFile saved = service.createFile(filesClass);

        verify(repository, times(1)).save(any());
        assertThat(saved.getName()).isEqualTo(tempFile.getFileName().toString().replaceFirst("[.][^.]+$", ""));

        // Disk test
        File file = new File(System.getProperty("user.dir") + "/uploads/" + saved.getUuid() + "." + saved.getExtension());
        assertThat(file).exists();
        file.delete();
    }

    @Test
    void givenInvalidFile_whenCreateFile_thenReturnEmpty() {
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "file",
                "",
                "",
                new byte[0]
        );

        FilesClass filesClass = new FilesClass(null, mockMultipartFile);

        CustomFile created = service.createFile(filesClass);

        assertThat(created).isNull();
    }


    @Test
    void givenValidId_whenDeleteFile_thenFileIsDeleted() throws IOException {
        Path tempFile = Files.createTempFile("test", ".png");

        byte[] content = "Hello, World!".getBytes();
        Files.write(tempFile, content);

        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "file",                             // parameter name
                tempFile.getFileName().toString(),      // file name
                "image/png",                // content type
                Files.readAllBytes(tempFile)            // content as byte array
        );

        // Repository test
        FilesClass filesClass = new FilesClass(null, mockMultipartFile);
        CustomFile saved = service.createFile(filesClass);
        
        when(repository.findById(1L)).thenReturn(Optional.of(saved));

        File file = new File(System.getProperty("user.dir") + "/uploads/" + saved.getUuid() + "." + saved.getExtension());
        assertThat(file).exists();

        service.deleteFile(1L);

        assertThat(repository.findByName(saved.getName())).isEmpty();
        verify(repository, times(1)).delete(saved);
        assertThat(file).doesNotExist();
    }

    @Test
    void givenInvalidId_whenDeleteFile_thenReturnFalse() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        boolean deleted = service.deleteFile(1L);

        assertThat(deleted).isFalse();
        verify(repository, times(0)).delete(any());
    }


    @Test
    void givenValidId_whenDownloadFileById_ThenReturnResource() throws IOException {
        Path tempFile = Files.createTempFile("test", ".png");

        byte[] content = "Hello, World!".getBytes();
        Files.write(tempFile, content);

        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "file",                             // parameter name
                tempFile.getFileName().toString(),      // file name
                "image/png",                // content type
                Files.readAllBytes(tempFile)            // content as byte array
        );

        FilesClass filesClass = new FilesClass(null, mockMultipartFile);
        CustomFile saved = service.createFile(filesClass);

        // Paths to the saved file and the root directory
        String path = System.getProperty("user.dir") + "/uploads/" + saved.getUuid() + "." + saved.getExtension();
        
        when(repository.findById(1L)).thenReturn(Optional.of(saved));
        Resource response = service.downloadFileById(1L);

        assertThat(response).isNotNull();

        File file = new File(path);
        assertTrue(file.exists());

        file.delete();
    }

    @Test
    void givenInvalidId_whenDownloadFileById_thenReturnNull() throws MalformedURLException {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        Resource file = service.downloadFileById(1L);

        assertThat(file).isNull();
    }

    @Test
    void givenValidId_whenUpdateFileName_thenReturnUpdatedFile() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(customFile));
        
        customFile.setName("NewName");

        when(repository.save(any())).thenReturn(customFile);

        CustomFile updatedFile = service.updateFileName(1L, "NewName");
        
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(any());
        assertThat(updatedFile.getName()).isEqualTo("NewName"); 
    }

    @Test
    void givenInvalidId_whenUpdateFileName_thenReturnNull() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        CustomFile saved = service.updateFileName(1L,"");

        assertThat(saved).isNull();
        verify(repository, times(0)).save(any());
    }

    @Test
    void givenValidFile_whenCreateFileInsideDirectory_thenFileIsSaved() throws IOException {
        Path tempFile = Files.createTempFile("test", ".png");

        byte[] content = "Hello, World!".getBytes();
        Files.write(tempFile, content);

        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "file",                                 // parameter name
                tempFile.getFileName().toString(),      // file name
                "image/png",                            // content type
                Files.readAllBytes(tempFile)            // content as byte array
        );

        // Repository test
        when(repository.findById(1L)).thenReturn(Optional.of(customFile3));

        FilesClass filesClass = new FilesClass(1L, mockMultipartFile);
        CustomFile saved = service.createFile(filesClass);

        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(3)).save(any());
        assertThat(saved.getName()).isEqualTo(tempFile.getFileName().toString().replaceFirst("[.][^.]+$", ""));
        assertThat(saved.getParent()).isEqualTo(customFile3);

        // Disk test
        File file = new File(System.getProperty("user.dir") + "/uploads/" + saved.getUuid() + "." + saved.getExtension());
        assertThat(file).exists();
        file.delete();
    }

}
