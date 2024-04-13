package deti.uas.uasmartsignage.initializer;

import java.util.List;

import deti.uas.uasmartsignage.Models.CustomFile;
import deti.uas.uasmartsignage.Repositories.FileRepository;
import deti.uas.uasmartsignage.Services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

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

        }
}
