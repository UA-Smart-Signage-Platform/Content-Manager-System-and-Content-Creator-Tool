package deti.uas.uasmartsignage.initializer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import deti.uas.uasmartsignage.Models.CustomFile;
import deti.uas.uasmartsignage.Models.FilesClass;
import deti.uas.uasmartsignage.Repositories.FileRepository;
import deti.uas.uasmartsignage.Services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import deti.uas.uasmartsignage.Models.Monitor;
import deti.uas.uasmartsignage.Models.MonitorsGroup;
import deti.uas.uasmartsignage.Repositories.MonitorGroupRepository;
import deti.uas.uasmartsignage.Repositories.MonitorRepository;

@Component
@Profile("!test")
public class DataLoader implements CommandLineRunner {
        private MonitorRepository monitorRepository;
        private MonitorGroupRepository groupRepository;
        private FileRepository fileRepository;
        private FileService fileService;

        private Path tempFile;
        private Path tempFile1;
        private Path tempFile2;
        private Path tempFile3;


        @Autowired
        public DataLoader(MonitorGroupRepository groupRepository, MonitorRepository monitorRepository, FileRepository fileRepository, FileService fileService){
            this.groupRepository = groupRepository;
            this.monitorRepository = monitorRepository;
            this.fileRepository = fileRepository;
            this.fileService = fileService;
            
        }

        public void run(String ...args) throws Exception{
            if (!groupRepository.findAll().isEmpty()){
                return;
            }

            MonitorsGroup deti = new MonitorsGroup();
            deti.setName("deti");
            deti.setMonitors(List.of());

            MonitorsGroup dMat = new MonitorsGroup();
            dMat.setName("dMat");
            dMat.setMonitors(List.of());

            MonitorsGroup dBio = new MonitorsGroup();
            dBio.setName("dBio");
            dBio.setMonitors(List.of());

            deti = groupRepository.saveAndFlush(deti);
            dMat = groupRepository.saveAndFlush(dMat);
            dBio = groupRepository.saveAndFlush(dBio);

            Monitor hall = new Monitor();
            hall.setIp("192.168.1");
            hall.setName("hall");
            hall.setPending(false);
            hall.setGroup(deti);
            monitorRepository.save(hall);
            
            Monitor flow = new Monitor();
            flow.setIp("192.168.2");
            flow.setName("flow");
            flow.setPending(false);
            flow.setGroup(deti);
            monitorRepository.save(flow);

            Monitor door = new Monitor();
            door.setIp("192.168.3");
            door.setName("door");
            door.setPending(false);
            door.setGroup(dMat);
            monitorRepository.save(door);

            Monitor pipa = new Monitor();
            pipa.setIp("192.168.4");
            pipa.setName("pipa");
            pipa.setPending(false);
            pipa.setGroup(dMat);
            monitorRepository.save(pipa);

            Monitor train = new Monitor();
            train.setIp("192.168.5");
            train.setName("train");
            train.setPending(false);
            train.setGroup(dBio);
            monitorRepository.save(train);

            Monitor car = new Monitor();
            car.setIp("192.168.6");
            car.setName("car");
            car.setPending(true);
            car.setGroup(dMat);
            monitorRepository.save(car);

            Monitor car2 = new Monitor();
            car2.setIp("192.168.7");
            car2.setName("car2");
            car2.setPending(true);
            car2.setGroup(dBio);
            monitorRepository.save(car2);

            /*Path tempFile = Files.createTempFile("test", ".png");
            byte[] content = "Hello, World!".getBytes();
            Files.write(tempFile, content);*/

            CustomFile fileEntity = new CustomFile();
            fileEntity.setName("test");
            fileEntity.setType("image/png");
            fileEntity.setParent(null);
            fileEntity.setSize(10L);
            fileEntity.setPath("test");
            fileRepository.save(fileEntity);



            /*tempFile1 = Files.createTempFile("test1", ".png");
            byte[] content1 = "Hello!".getBytes();
            Files.write(tempFile1, content1);*/

            CustomFile file1 = new CustomFile();
            file1.setName("lei");
            file1.setType("image/png");
            file1.setParent(null);
            file1.setPath("lei");
            file1.setId(1L);
            file1.setSize(10L);
            fileRepository.save(file1);
            //Files.copy(tempFile1, Paths.get(file1.getPath()), StandardCopyOption.REPLACE_EXISTING);

            /*Path tempDir = Files.createTempDirectory("videos");
            CustomFile directoryEntity = new CustomFile();
            directoryEntity.setName("videos");
            directoryEntity.setType("directory");
            directoryEntity.setParent(null);
            directoryEntity.setPath(tempDir.toString());
            directoryEntity.setSize(0L);
            fileRepository.save(directoryEntity);
            fileService.createDirectory(directoryEntity);*/

            CustomFile directoryEntity = new CustomFile();
            directoryEntity.setName("videos");
            directoryEntity.setType("directory");
            directoryEntity.setParent(null);
            directoryEntity.setPath("videos");
            directoryEntity.setSize(0L);
            fileRepository.save(directoryEntity);



            /*tempFile2 = Files.createTempFile("test2", ".mp4");
            byte[] content3 = "Goodbye".getBytes();
            Files.write(tempFile2, content3);*/
            CustomFile file2 = new CustomFile();
            file2.setName("lei2");
            file2.setType("video/mp4");
            file2.setParent(directoryEntity);
            file2.setPath("lei2");
            file2.setSize(10L);
            fileRepository.save(file2);
            //Files.copy(tempFile2, Paths.get(file2.getPath()), StandardCopyOption.REPLACE_EXISTING);


        }
}
