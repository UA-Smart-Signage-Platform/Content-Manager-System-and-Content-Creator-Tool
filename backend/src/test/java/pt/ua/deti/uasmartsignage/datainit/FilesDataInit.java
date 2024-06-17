package pt.ua.deti.uasmartsignage.datainit;


import java.nio.charset.StandardCharsets;

import pt.ua.deti.uasmartsignage.models.AppUser;
import pt.ua.deti.uasmartsignage.models.CustomFile;
import pt.ua.deti.uasmartsignage.models.FilesClass;
import pt.ua.deti.uasmartsignage.services.FileService;
import pt.ua.deti.uasmartsignage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import pt.ua.deti.uasmartsignage.repositories.MonitorGroupRepository;

@Component
@Profile("integration-test")
public class FilesDataInit implements CommandLineRunner{
    private MonitorGroupRepository groupRepository;
    private FileService fileService;
    private UserService userService;

    @Autowired
    public FilesDataInit(MonitorGroupRepository groupRepository, FileService fileService, UserService userService){
        this.groupRepository = groupRepository;
        this.fileService = fileService;
        this.userService = userService;
    }


    public void run(String ...args) throws Exception{
        if (!groupRepository.findAll().isEmpty()){
            System.out.println("Data already exists");
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

        AppUser admin = new AppUser();
        admin.setEmail("admin");
        admin.setRole("ADMIN");
        admin.setPassword("admin");
        userService.saveAdminUser(admin);
    }
}