package deti.uas.uasmartsignage;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Profile;

import deti.uas.uasmartsignage.Models.CustomFile;
import deti.uas.uasmartsignage.Models.Monitor;
import deti.uas.uasmartsignage.Models.Content;
import deti.uas.uasmartsignage.Models.Template;
import deti.uas.uasmartsignage.Models.User;
import deti.uas.uasmartsignage.Models.MonitorsGroup;
import deti.uas.uasmartsignage.Models.TemplateGroup;
import deti.uas.uasmartsignage.Models.Widget;
import deti.uas.uasmartsignage.Models.TemplateWidget;
import deti.uas.uasmartsignage.Services.FileService;
import deti.uas.uasmartsignage.Services.MonitorService;
import deti.uas.uasmartsignage.Services.UserService;
import deti.uas.uasmartsignage.Services.TemplateGroupService;
import deti.uas.uasmartsignage.Services.TemplateService;
import deti.uas.uasmartsignage.Services.WidgetService;
import deti.uas.uasmartsignage.Services.ContentService;
import deti.uas.uasmartsignage.Services.TemplateWidgetService;
import deti.uas.uasmartsignage.Services.MonitorGroupService;
import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class UasmartsignageApplication {
	/*
	private final FileService fileService;
	private final MonitorService monitorService;
	private final UserService userService;
	private final TemplateGroupService templateGroupService;
	private final TemplateService templateService;
	private final WidgetService widgetService;
	private final ContentService contentService;
	private final TemplateWidgetService templateWidgetService;
	private final MonitorGroupService monitorGroupService;


    // public UasmartsignageApplication(FileService fileService, MonitorService monitorService, UserService userService, TemplateGroupService templateGroupService, TemplateService templateService, WidgetService widgetService, ContentService contentService, TemplateWidgetService templateWidgetService, MonitorGroupService monitorGroupService) {
    //     this.fileService = fileService;
	// 	this.monitorService = monitorService;
	// 	this.userService = userService;
	// 	this.templateGroupService = templateGroupService;
	// 	this.templateService = templateService;
	// 	this.widgetService = widgetService;
	// 	this.contentService = contentService;
	// 	this.templateWidgetService = templateWidgetService;
	// 	this.monitorGroupService = monitorGroupService;
    // }

	
	@PostConstruct
    public void initialize() throws Exception {
        System.out.println("Creating mock directory");
        if (!fileService.getAllFiles().isEmpty()) {
            System.out.println("Files already exist");
            return;
        }
        CustomFile file = new CustomFile();
        file.setName("Mock Directory");
        file.setType("directory");
        file.setParent(null);
        List<CustomFile> subDirectories =  List.of();
        file.setSubDirectories(subDirectories);
        System.out.println("Creating file: " + file);
        fileService.createFile(file);

	// 	// Create MonitorsGroup

	// 	MonitorsGroup monitorsGroup = new MonitorsGroup();
	// 	monitorsGroup.setName("MonitorsGroup");
	// 	monitorsGroup.setMonitors(List.of());
	// 	monitorsGroup.setTemplateGroup(null);
	// 	monitorGroupService.saveGroup(monitorsGroup);

	// 	//Create Monitor

	// 	Monitor monitor = new Monitor();
	// 	monitor.setName("Aveiro");
	// 	monitor.setGroup(monitorsGroup);
	// 	monitor.setPending(false);
	// 	monitor.setIp("192.1.20");
	// 	monitorService.saveMonitor(monitor);

	// 	//Create Content

	// 	Content content = new Content();
	// 	content.setName("Video");
	// 	content.setType("video");
	// 	content.setDescription("Video Description");
	// 	contentService.saveContent(content);

	// 	//create Widget

	// 	Widget widget = new Widget();
	// 	widget.setName("Widget");
	// 	widget.setPath("path");
	// 	widget.setContent(content);
	// 	widgetService.saveWidget(widget);


	// 	//Create Template

	// 	Template template = new Template();
	// 	template.setPath("path");
	// 	template.setName("Template");
	// 	templateService.saveTemplate(template);

	// 	//Create TemplateWidget

	// 	TemplateWidget templateWidget = new TemplateWidget();

	// 	templateWidget.setName("TemplateWidget");
	// 	templateWidget.setTop(1L);
	// 	templateWidget.setLeftPosition(1L);
	// 	templateWidget.setWidth(1L);
	// 	templateWidget.setHeight(1L);
	// 	templateWidget.setTemplate(template);
	// 	templateWidget.setWidget(widget);
	// 	templateWidgetService.saveTemplateWidget(templateWidget);

	// 	//Create User

	// 	User user = new User();
	// 	user.setUsername("username");
	// 	user.setRole(1);
	// 	userService.saveUser(user);

	// 	//Create TemplateGroup

		TemplateGroup templateGroup = new TemplateGroup();
		templateGroup.setTemplate(template);
		templateGroup.setMonitorsGroupForTemplate(monitorsGroup);
		templateGroup.setContent(null);
		templateGroupService.saveGroup(templateGroup);
    }
	*/

    public static void main(String[] args) {
        SpringApplication.run(UasmartsignageApplication.class, args);
    }


}
