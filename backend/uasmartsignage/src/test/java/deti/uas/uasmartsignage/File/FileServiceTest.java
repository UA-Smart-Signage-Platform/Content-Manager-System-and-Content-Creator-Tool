package deti.uas.uasmartsignage.File;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import deti.uas.uasmartsignage.Models.CustomFile;
import deti.uas.uasmartsignage.Models.FilesClass;
import deti.uas.uasmartsignage.Repositories.FileRepository;
import deti.uas.uasmartsignage.Services.FileService;

@ExtendWith(MockitoExtension.class)
public class FileServiceTest {
    
    @Mock(lenient = true)
    private FileRepository repository;

    @InjectMocks
    private FileService fileService;

    @BeforeEach
    void setUp(){
        CustomFile customFile = new CustomFile("DETI", "directory", 0L, null, List.of());
        CustomFile customFile2 = new CustomFile("50 Anos", "directory", 0L, customFile, List.of());
        CustomFile customFile3 = new CustomFile("DMAT", "directory", 0L, null, List.of());

        customFile.setId(111L);
        customFile2.setId(222L);

        List<CustomFile> allFiles = Arrays.asList(customFile, customFile2, customFile3);

        Mockito.when(repository.findById(customFile.getId())).thenReturn(Optional.of(customFile));
        Mockito.when(repository.findById(customFile2.getId())).thenReturn(Optional.of(customFile2));
        Mockito.when(repository.findAll()).thenReturn(allFiles);
    }

    @Test
    void smth(){
        fileService.createDirectory(repository.findById(111L).get());
        fileService.createDirectory(repository.findById(222L).get());
    }
}
