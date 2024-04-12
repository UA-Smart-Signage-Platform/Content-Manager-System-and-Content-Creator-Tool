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
            hall.setUuid("a809f305-ebba-4cc0-a204-724ac97ca655");
            hall.setName("hall");
            hall.setPending(false);
            hall.setGroup(deti);
            monitorRepository.save(hall);
            
            Monitor flow = new Monitor();
            flow.setUuid("1d550453-2866-4194-8fdc-94fe15d5684c");
            flow.setName("flow");
            flow.setPending(false);
            flow.setGroup(deti);
            monitorRepository.save(flow);

            Monitor door = new Monitor();
            door.setUuid("2acf1449-e3a2-48f4-9255-7daf75277c85");
            door.setName("door");
            door.setPending(false);
            door.setGroup(dMat);
            monitorRepository.save(door);

            Monitor pipa = new Monitor();
            pipa.setUuid("1248922f-0e0f-4e8b-822f-bf98ac028c1c");
            pipa.setName("pipa");
            pipa.setPending(false);
            pipa.setGroup(dMat);
            monitorRepository.save(pipa);

            Monitor train = new Monitor();
            train.setUuid("f1771e56-4808-4f2b-afb9-31a4e485ee2b");
            train.setName("train");
            train.setPending(false);
            train.setGroup(dBio);
            monitorRepository.save(train);

            Monitor car = new Monitor();
            car.setUuid("015462fe-3f75-4f10-b8b5-d851ee60016d");
            car.setName("car");
            car.setPending(true);
            car.setGroup(dMat);
            monitorRepository.save(car);

            Monitor car2 = new Monitor();
            car2.setUuid("0f6a21fa-fbe1-4d9a-9ba1-fce9f1922f6f");
            car2.setName("car2");
            car2.setPending(true);
            car2.setGroup(dBio);
            monitorRepository.save(car2);
        }
}
