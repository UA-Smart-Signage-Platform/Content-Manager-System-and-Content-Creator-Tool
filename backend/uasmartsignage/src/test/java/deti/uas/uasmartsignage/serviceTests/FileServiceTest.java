package deti.uas.uasmartsignage.serviceTests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    private FileService service;


    @Test
    void whenGetFileById_thenReturnFile() {
        CustomFile customFile = new CustomFile("New directory", "directory", 0L, null, List.of());

        when(repository.findById(1L)).thenReturn(Optional.of(customFile));

        CustomFile found = service.getFileById(1L);

        assertThat(found).isEqualTo(customFile);
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void whenSaveFolder_thenFolderIsSaved() {
        CustomFile customFile = new CustomFile("New directory", "directory", 0L, null, List.of());

        when(repository.save(customFile)).thenReturn(customFile);

        CustomFile saved = service.createDirectory(customFile);

        assertThat(saved).isEqualTo(customFile);
        verify(repository, times(1)).save(customFile);
    }

    @Test
    void whenSaveFile_thenFileIsSaved() {
        //FilesClass filesClass = new FilesClass(null, null);
    }

    @Test
    void whenSaveFolderInsideFolder_thenFolderIsSavedInsideFolder() {
        CustomFile outerFolder = new CustomFile("Outer directory", "directory", 0L, null, List.of());
        CustomFile innerFolder = new CustomFile("Inner directory", "directory", 0L, outerFolder, List.of());

        when(repository.save(outerFolder)).thenReturn(outerFolder);
        when(repository.save(innerFolder)).thenReturn(innerFolder);

        CustomFile savedOuter = service.createDirectory(outerFolder);
        CustomFile savedInnerFolder = service.createDirectory(innerFolder);

        assertThat(savedOuter).isEqualTo(outerFolder);
        assertThat(savedInnerFolder).isEqualTo(savedInnerFolder);
        assertThat(savedOuter.getSubDirectories().size()).isEqualTo(1);
        verify(repository, times(2)).save(Mockito.any());
    }

    @Test
    void whenSaveFileInsideFolder_thenFileIsSavedInsideFolder() {
        //FilesClass filesClass = new FilesClass(null, null);
    }
}
