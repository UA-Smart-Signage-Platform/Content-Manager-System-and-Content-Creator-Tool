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
    public DataLoader(TemplateWidgetRepository templateWidgetRepository, TemplateRepository templateRepository,
            ContentRepository contentRepository, WidgetRepository widgetRepository,
            MonitorGroupRepository groupRepository, MonitorRepository monitorRepository) {
        this.templateWidgetRepository = templateWidgetRepository;
        this.templateRepository = templateRepository;
        this.contentRepository = contentRepository;
        this.widgetRepository = widgetRepository;
        this.groupRepository = groupRepository;
        this.monitorRepository = monitorRepository;
    }

    public void run(String... args) throws Exception {
        if (!groupRepository.findAll().isEmpty()) {
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

    private void loadTemplates() {

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
        Content mediaWidgetContent = new Content();
        mediaWidgetContent.setName("videos");
        mediaWidgetContent.setType("media");
        mediaWidgetContent.setWidget(mediaWidget);
        contentRepository.save(mediaWidgetContent);

        Widget imageWidget = new Widget();
        imageWidget.setName("Image");
        imageWidget.setPath("static/widgets/image.widget");
        widgetRepository.save(imageWidget);
        Content imageWidgetContent = new Content();
        imageWidgetContent.setName("image");
        imageWidgetContent.setType("media");
        imageWidgetContent.setWidget(imageWidget);
        contentRepository.save(imageWidgetContent);

        Widget timeWidget = new Widget();
        timeWidget.setName("CurrentTime");
        timeWidget.setPath("static/widgets/time.widget");
        timeWidget.setContents(new ArrayList<>());
        widgetRepository.save(timeWidget);

        Widget newsWidget = new Widget();
        newsWidget.setName("News");
        newsWidget.setPath("static/widgets/news.widget");
        newsWidget.setContents(new ArrayList<>());
        widgetRepository.save(newsWidget);

        // create template1
        Template template1 = new Template();
        template1.setName("template1");
        templateRepository.save(template1);

        // create the widget mappings for template 1
        TemplateWidget temperature = new TemplateWidget();
        temperature.setTop(0);
        temperature.setLeftPosition(0);
        temperature.setHeight(10);
        temperature.setWidth(20);
        temperature.setTemplate(template1);
        temperature.setWidget(temperatureWidget);
        templateWidgetRepository.save(temperature);

        TemplateWidget video = new TemplateWidget();
        video.setTop(10);
        video.setLeftPosition(20);
        video.setHeight(80);
        video.setWidth(80);
        video.setTemplate(template1);
        video.setWidget(mediaWidget);
        templateWidgetRepository.save(video);

        TemplateWidget clock = new TemplateWidget();
        clock.setTop(0);
        clock.setLeftPosition(80);
        clock.setHeight(10);
        clock.setWidth(20);
        clock.setTemplate(template1);
        clock.setWidget(timeWidget);
        templateWidgetRepository.save(clock);

        TemplateWidget news = new TemplateWidget();
        news.setTop(90);
        news.setLeftPosition(0);
        news.setHeight(10);
        news.setWidth(100);
        news.setTemplate(template1);
        news.setWidget(newsWidget);
        templateWidgetRepository.save(news);

        TemplateWidget image = new TemplateWidget();
        image.setTop(10);
        image.setLeftPosition(0);
        image.setHeight(80);
        image.setWidth(20);
        image.setTemplate(template1);
        image.setWidget(imageWidget);
        templateWidgetRepository.save(image);

        // create template2
        Template template2 = new Template();
        template2.setName("template2");
        templateRepository.save(template2);

        // create the widget mappings for template 2
        temperature = new TemplateWidget();
        temperature.setTop(0);
        temperature.setLeftPosition(0);
        temperature.setHeight(20);
        temperature.setWidth(20);
        temperature.setTemplate(template2);
        temperature.setWidget(temperatureWidget);
        templateWidgetRepository.save(temperature);

        video = new TemplateWidget();
        video.setTop(0);
        video.setLeftPosition(20);
        video.setHeight(90);
        video.setWidth(80);
        video.setTemplate(template2);
        video.setWidget(mediaWidget);
        templateWidgetRepository.save(video);

        news = new TemplateWidget();
        news.setTop(90);
        news.setLeftPosition(0);
        news.setHeight(10);
        news.setWidth(100);
        news.setTemplate(template2);
        news.setWidget(newsWidget);
        templateWidgetRepository.save(news);

        image = new TemplateWidget();
        image.setTop(20);
        image.setLeftPosition(0);
        image.setHeight(70);
        image.setWidth(20);
        image.setTemplate(template2);
        image.setWidget(imageWidget);
        templateWidgetRepository.save(image);
    }
}
