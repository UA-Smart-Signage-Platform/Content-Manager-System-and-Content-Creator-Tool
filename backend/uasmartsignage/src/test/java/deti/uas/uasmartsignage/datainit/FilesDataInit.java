package deti.uas.uasmartsignage.datainit;


import java.nio.charset.StandardCharsets;

import deti.uas.uasmartsignage.Models.CustomFile;
import deti.uas.uasmartsignage.Models.FilesClass;
import deti.uas.uasmartsignage.Services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import deti.uas.uasmartsignage.Repositories.MonitorGroupRepository;

@Component
@Profile("integration-test")
public class FilesDataInit implements CommandLineRunner{
    private MonitorGroupRepository groupRepository;
    private FileService fileService;

    @Autowired
    public FilesDataInit(MonitorGroupRepository groupRepository, FileService fileService){
        this.groupRepository = groupRepository;
        this.fileService = fileService;
    }


    public void run(String ...args) throws Exception{
        if (!groupRepository.findAll().isEmpty()){
            return;
        }

        CustomFile testDir = new CustomFile();
        testDir.setName("testDir");
        testDir.setParent(null);
        testDir.setSize(0L);
        testDir.setType("directory");
        fileService.createDirectory(testDir);

        byte[] content = "This is a test file content".getBytes(StandardCharsets.UTF_8);
        MultipartFile file = new MockMultipartFile("file", "test.png", "image/png", content);
        FilesClass ff = new FilesClass();
        ff.setFile(file);
        ff.setParentId(1L);
        fileService.createFile(ff);

        MultipartFile file1 = new MockMultipartFile("file", "test1.png", "image/png", content);
        FilesClass ff1 = new FilesClass();
        ff1.setFile(file1);
        ff1.setParentId(1L);
        fileService.createFile(ff1);

    }
}