package deti.uas.uasmartsignage.serviceTests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
    
    @Mock(lenient = true)
    private FileRepository repository;

    @InjectMocks
    private FileService service;


    @Test
    @Disabled
    void whenGetFileById_thenReturnFile() {
        CustomFile customFile = new CustomFile("New directory", "directory", 0L, null, List.of());

        when(repository.findById(1L)).thenReturn(Optional.of(customFile));

        CustomFile found = service.getFileById(1L);

        assertThat(found).isEqualTo(customFile);
        verify(repository, times(1)).findById(1L);
    }

    @Test
    @Disabled
    void whenSaveFolder_thenFolderIsSaved() {
        CustomFile customFile = new CustomFile("New directory", "directory", 0L, null, new ArrayList<>());

        when(repository.save(customFile)).thenReturn(customFile);

        CustomFile saved = service.createDirectory(customFile);

        assertThat(saved).isEqualTo(customFile);
        verify(repository, times(1)).save(customFile);
    }

    @Test
    @Disabled
    void whenSaveFolderInsideFolder_thenFolderIsSavedInsideFolder() {
        CustomFile outerFolder = new CustomFile("Outer directory", "directory", 0L, null, new ArrayList<>());
        CustomFile innerFolder = new CustomFile("Inner directory", "directory", 0L, outerFolder, new ArrayList<>());

        when(repository.save(outerFolder)).thenReturn(outerFolder);
        when(repository.save(innerFolder)).thenReturn(innerFolder);

        CustomFile savedOuter = service.createDirectory(outerFolder);
        CustomFile savedInner = service.createDirectory(innerFolder);

        assertThat(savedOuter).isEqualTo(outerFolder);
        assertThat(savedInner).isEqualTo(innerFolder);
        assertThat(savedOuter.getSubDirectories().size()).isEqualTo(1);
        verify(repository, times(2)).save(Mockito.any());
    }

    @Test
    void whenSaveFileInsideFolder_thenFileIsSavedInsideFolder() {
        CustomFile outerFolder = new CustomFile("Outer directoroe", "directory", 0L, null, new ArrayList<>());

        when(repository.save(outerFolder)).thenReturn(outerFolder);
        when(repository.findById(1L)).thenReturn(Optional.of(outerFolder));
        
        service.createDirectory(outerFolder);


        CustomFile innerFolder = new CustomFile("Inner directoroe", "directory", 0L, outerFolder, new ArrayList<>());

        when(repository.save(innerFolder)).thenReturn(innerFolder);
        when(repository.findById(1L)).thenReturn(Optional.of(innerFolder));

        service.createDirectory(innerFolder);

        try {
            File file = ResourceUtils.getFile(System.getProperty("user.dir") + "/src/main/resources/file1.png");
            File file2 = ResourceUtils.getFile(System.getProperty("user.dir") + "/src/main/resources/file2.png");
            File file3 = ResourceUtils.getFile(System.getProperty("user.dir") + "/src/main/resources/file3.png");

            // Convert the file into a byte array
            FileInputStream input = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            input.read(bytes);
            input.close();

            FileInputStream input2 = new FileInputStream(file2);
            byte[] bytes2 = new byte[(int) file2.length()];
            input2.read(bytes2);
            input2.close();

            FileInputStream input3 = new FileInputStream(file3);
            byte[] bytes3 = new byte[(int) file3.length()];
            input3.read(bytes3);
            input3.close();

            // Create a mock MultipartFile object
            MockMultipartFile mockMultipartFile = new MockMultipartFile(
                    "file",           // parameter name
                    file.getName(),   // original file name
                    "image/png",     // content type
                    bytes            // content as byte array
            );

            MockMultipartFile mockMultipartFile2 = new MockMultipartFile(
                    "file",           // parameter name
                    file2.getName(),   // original file name
                    "image/png",     // content type
                    bytes2            // content as byte array
            );

            MockMultipartFile mockMultipartFile3 = new MockMultipartFile(
                "file",           // parameter name
                file3.getName(),   // original file name
                "image/png",     // content type
                bytes3            // content as byte array
        );

            FilesClass filesClass = new FilesClass(innerFolder, mockMultipartFile);
            FilesClass filesClass2 = new FilesClass(innerFolder, mockMultipartFile2);
            FilesClass filesClass3 = new FilesClass(null, mockMultipartFile3);

            service.createFile(filesClass);
            service.createFile(filesClass2);
            service.createFile(filesClass3);

        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}