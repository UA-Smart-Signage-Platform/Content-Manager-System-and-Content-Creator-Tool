package deti.uas.uasmartsignage.initializer;

import java.util.List;

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

        @Autowired
        public DataLoader(MonitorGroupRepository groupRepository, MonitorRepository monitorRepository){
            this.groupRepository = groupRepository;
            this.monitorRepository = monitorRepository;
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
            hall.setUuid("hall");
            hall.setName("hall");
            hall.setPending(false);
            hall.setGroup(deti);
            monitorRepository.save(hall);
            
            Monitor flow = new Monitor();
            flow.setUuid("flow");
            flow.setName("flow");
            flow.setPending(false);
            flow.setGroup(deti);
            monitorRepository.save(flow);

            Monitor door = new Monitor();
            door.setUuid("door");
            door.setName("door");
            door.setPending(false);
            door.setGroup(dMat);
            monitorRepository.save(door);

            Monitor pipa = new Monitor();
            pipa.setUuid("pipa");
            pipa.setName("pipa");
            pipa.setPending(false);
            pipa.setGroup(dMat);
            monitorRepository.save(pipa);

            Monitor train = new Monitor();
            train.setUuid("train");
            train.setName("train");
            train.setPending(false);
            train.setGroup(dBio);
            monitorRepository.save(train);

            Monitor car = new Monitor();
            car.setUuid("car");
            car.setName("car");
            car.setPending(true);
            car.setGroup(dMat);
            monitorRepository.save(car);

            Monitor car2 = new Monitor();
            car2.setUuid("car2");
            car2.setName("car2");
            car2.setPending(true);
            car2.setGroup(dBio);
            monitorRepository.save(car2);
        }
}
