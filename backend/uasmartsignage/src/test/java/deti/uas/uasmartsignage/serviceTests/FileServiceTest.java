package deti.uas.uasmartsignage.serviceTests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.ResourceUtils;

import deti.uas.uasmartsignage.Models.CustomFile;
import deti.uas.uasmartsignage.Models.FilesClass;
import deti.uas.uasmartsignage.Repositories.FileRepository;
import deti.uas.uasmartsignage.Services.FileService;

@ExtendWith(MockitoExtension.class)
public class FileServiceTest {
    
    @Mock
    private FileRepository repository;

    @InjectMocks
    private FileService service;

    // TODO - create and revise

    @Test
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
    void whenSaveFolder_thenFolderIsSaved() {
        CustomFile customFile = new CustomFile("New directory", "directory", 0L, null);

        when(repository.save(customFile)).thenReturn(customFile);

        CustomFile saved = service.createDirectory(customFile);

        assertThat(saved).isEqualTo(customFile);
        verify(repository, times(1)).save(customFile);

        String parentDirectoryPath = service.getParentDirectoryPath(customFile);
        File directory = new File(parentDirectoryPath + customFile.getName());

        assertThat(directory).exists();

        // Clean up
        directory.delete();
    }

    @Test
    void whenSaveFolderInsideFolder_thenFolderIsSavedInsideFolder() {
        CustomFile outerFolder = new CustomFile("Outer directory", "directory", 0L, null);

        service.createDirectory(outerFolder);

        String parentDirectoryPathOuterF = service.getParentDirectoryPath(outerFolder);
        File outerDirectory = new File(parentDirectoryPathOuterF + outerFolder.getName());

        assertThat(outerDirectory).exists();

        CustomFile innerFolder = new CustomFile("Inner directory", "directory", 0L, outerFolder);
        when(repository.save(innerFolder)).thenReturn(innerFolder);

        CustomFile savedInner = service.createDirectory(innerFolder);

        assertThat(savedInner).isEqualTo(innerFolder);
        verify(repository, times(1)).save(innerFolder);

        String parentDirectoryPathInnerF = service.getParentDirectoryPath(innerFolder);
        File innerDirectory = new File(parentDirectoryPathInnerF + innerFolder.getName());

        assertThat(innerDirectory).exists();

        // Clean up
        if (innerDirectory.exists()) {
            innerDirectory.delete();
        }
        if (outerDirectory.exists()) {
            outerDirectory.delete();
        }
    }


    @Test
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
        when(repository.save(any())).thenReturn(saved);
        verify(repository, times(1)).save(any());

        //Clean up
        Files.deleteIfExists(tempFile);
    }

    @Test
    void whenSaveFileInsideFolder_thenFileIsSavedInsideFolder() throws IOException {
        CustomFile outerFolder = new CustomFile("Outer directory", "directory", 0L, null);
        outerFolder.setId(1L);

        service.createDirectory(outerFolder);

        String parentDirectoryPathOuterF = service.getParentDirectoryPath(outerFolder);
        File outerDirectory = new File(parentDirectoryPathOuterF + outerFolder.getName());

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


        when(repository.findById(1L)).thenReturn(Optional.of(outerFolder));


        FilesClass filesClass = new FilesClass(outerFolder.getId(), mockMultipartFile);
        CustomFile savedInner = service.createFile(filesClass);


        String parentDirectoryPathInnerF = service.getParentDirectoryPath(savedInner);
        File innerDirectory = new File(parentDirectoryPathInnerF + savedInner.getName());

        assertThat(innerDirectory).exists();
        verify(repository, times(2)).save(any());

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
    void whenDeleteFile_thenFileIsDeleted() throws IOException {
        CustomFile customFile = new CustomFile("New directory", "directory", 0L, null);
        customFile.setId(1L);

        service.createDirectory(customFile);

        when(repository.findById(1L)).thenReturn(Optional.of(customFile));

        service.deleteFile(1L);

        String parentDirectoryPath = service.getParentDirectoryPath(customFile);
        File directory = new File(parentDirectoryPath + customFile.getName());

        // Check if the file was deleted from the repository and from the disk
        assertThat(repository.findByName(customFile.getName())).isEmpty();
        assertThat(directory).doesNotExist();
        verify(repository, times(1)).delete(customFile);
    }

}
