package deti.uas.uasmartsignage.initializer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import deti.uas.uasmartsignage.Models.Content;
import deti.uas.uasmartsignage.Models.Monitor;
import deti.uas.uasmartsignage.Models.MonitorsGroup;
import deti.uas.uasmartsignage.Models.Template;
import deti.uas.uasmartsignage.Models.TemplateWidget;
import deti.uas.uasmartsignage.Models.Widget;
import deti.uas.uasmartsignage.Repositories.ContentRepository;
import deti.uas.uasmartsignage.Repositories.MonitorGroupRepository;
import deti.uas.uasmartsignage.Repositories.MonitorRepository;
import deti.uas.uasmartsignage.Repositories.TemplateRepository;
import deti.uas.uasmartsignage.Repositories.TemplateWidgetRepository;
import deti.uas.uasmartsignage.Repositories.WidgetRepository;

@Component
@Profile("!test")
public class DataLoader implements CommandLineRunner {
        private MonitorRepository monitorRepository;
        private MonitorGroupRepository groupRepository;
        private WidgetRepository widgetRepository;
        private ContentRepository contentRepository;
        private TemplateRepository templateRepository;
        private TemplateWidgetRepository templateWidgetRepository;

        @Autowired
        public DataLoader(TemplateWidgetRepository templateWidgetRepository, TemplateRepository templateRepository, ContentRepository contentRepository, WidgetRepository widgetRepository, MonitorGroupRepository groupRepository, MonitorRepository monitorRepository){
            this.templateWidgetRepository = templateWidgetRepository;
            this.templateRepository = templateRepository;
            this.contentRepository = contentRepository;
            this.widgetRepository = widgetRepository;
            this.groupRepository = groupRepository;
            this.monitorRepository = monitorRepository;
        }

        public void run(String ...args) throws Exception{
            if (!groupRepository.findAll().isEmpty()){
                return;
            }

            this.loadTemplates();

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
            hall.setWidth(1920);
            hall.setHeight(1080);
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

        private void loadTemplates(){

            // create the widgets and their contents
            Widget temperatureWidget = new Widget();
            temperatureWidget.setName("Temperature");
            temperatureWidget.setPath("static/widgets/temperature.widget");
            temperatureWidget.setContents(new ArrayList<>());
            widgetRepository.save(temperatureWidget);
            
            Widget mediaWidget = new Widget();
            mediaWidget.setName("Media");
            mediaWidget.setPath("static/widgets/media.widget");
            widgetRepository.save(mediaWidget);
            Content content = new Content();
            content.setName("videos");
            content.setType("media");
            content.setWidget(mediaWidget);
            contentRepository.save(content);

            // create template1
            Template template1 = new Template();
            template1.setName("template1");
            templateRepository.save(template1);

            // create the widget mappings for template 1
            TemplateWidget widget1 = new TemplateWidget();
            widget1.setTop(0);
            widget1.setLeftPosition(0);
            widget1.setHeight(10);
            widget1.setWidth(20);
            widget1.setTemplate(template1);
            widget1.setWidget(temperatureWidget);
            templateWidgetRepository.save(widget1);

            TemplateWidget widget2 = new TemplateWidget();
            widget2.setTop(10);
            widget2.setLeftPosition(20);
            widget2.setHeight(90);
            widget2.setWidth(80);
            widget2.setTemplate(template1);
            widget2.setWidget(mediaWidget);
            templateWidgetRepository.save(widget2);
        }
}
