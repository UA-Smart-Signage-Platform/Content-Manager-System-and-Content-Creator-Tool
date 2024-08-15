package pt.ua.deti.uasmartsignage.initializer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import pt.ua.deti.uasmartsignage.enums.WidgetVariableType;
import pt.ua.deti.uasmartsignage.models.AppUser;
import pt.ua.deti.uasmartsignage.models.Monitor;
import pt.ua.deti.uasmartsignage.models.MonitorGroup;
import pt.ua.deti.uasmartsignage.models.Rule;
import pt.ua.deti.uasmartsignage.models.Template;
import pt.ua.deti.uasmartsignage.models.Widget;
import pt.ua.deti.uasmartsignage.models.embedded.Schedule;
import pt.ua.deti.uasmartsignage.models.embedded.TemplateWidget;
import pt.ua.deti.uasmartsignage.repositories.MonitorGroupRepository;
import pt.ua.deti.uasmartsignage.repositories.MonitorRepository;
import pt.ua.deti.uasmartsignage.repositories.RuleRepository;
import pt.ua.deti.uasmartsignage.repositories.TemplateRepository;
import pt.ua.deti.uasmartsignage.repositories.WidgetRepository;
import pt.ua.deti.uasmartsignage.services.FileService;
import pt.ua.deti.uasmartsignage.services.UserService;

@Component
@Profile("!test & !integration-test")
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    @Value("${backend.production}")
    private String isProduction;

    private final MonitorRepository monitorRepository;
    private final MonitorGroupRepository groupRepository;
    private final WidgetRepository widgetRepository;
    private final TemplateRepository templateRepository;
    private final FileService fileService;
    private final UserService userService;
    private final RuleRepository ruleRepository;

    private Logger logger = org.slf4j.LoggerFactory.getLogger(DataLoader.class);

    public void run(String... args) throws Exception {
        if (!groupRepository.findAll().isEmpty()) {
            return;
        }

        // delete all files in the uploads folder if there are no files in the database
        if(fileService.getFilesAtRoot().isEmpty()){
            Path path = Paths.get("/app/uploads");
            try (Stream<Path> paths = Files.walk(path)) {
                paths.sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
            } catch (IOException e) {
                logger.error("Error deleting files in uploads folder");
            }
        }
            
        this.loadWidgetsAndTemplates();
        this.loadAdminUser();
        if(isProduction.equals("false")){
            this.loadGroupsAndMonitors();
        }
    }

    private void loadAdminUser() {
        // create the admin user
        AppUser admin = new AppUser();
        admin.setEmail("admin");
        admin.setRole("ADMIN");
        admin.setPassword("admin");
        userService.saveAdminUser(admin);
    }

    private void loadGroupsAndMonitors(){

        MonitorGroup deti = new MonitorGroup();
        deti.setName("DETI");
        deti.setDescription("Monitors from first floor, second and third");
        deti.setMonitors(List.of());

        MonitorGroup dMat = new MonitorGroup();
        dMat.setName("DMAT");
        dMat.setDescription("Monitors from first floor and bar");
        dMat.setMonitors(List.of());

        MonitorGroup dBio = new MonitorGroup();
        dBio.setName("DBIO");
        dBio.setDescription("Monitors from resting area");
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

    private void loadWidgetsAndTemplates() {

        // Temperature Widget
        List<String> stations = new ArrayList<>(List.of("Olhão, EPPO", "Tavira", "Graciosa / Serra das Fontes (DROTRH)", "Terras de Bouro/Barral (CIM)", "Amares Caldelas (CIM)", "Faja / Ilha das Flores", "Braga (CIM)", "Barcelos (CIM)", "Esposende (CIM)", "Sabugal, Martim Rei", "Viana do Castelo", "Nelas / Vilar Seco (CIM)", "Santa Maria / Praia Formosa (DROTRH)", "S. Miguel, Nordeste", "Oliveira de Frades (CIM)", "Setúbal, Areias", "Madeira, Porto Moniz", "Santa Maria / Maia (DROTRH)", "Madeira, S. Jorge, Santana", "Madeira, Santana", "Mação (CIM)", "Madeira, São Vicente", "Madeira, Bica da Cana", "Madeira, Pico Alto", "Madeira, Areeiro", "Madeira, Pico do Areeiro", "Madeira, Santo da Serra", "Madeira, Monte", "Madeira, Cancela", "Madeira, S. Lourenço", "Viseu (Cidade)", "Madeira, Lombo da Terça", "São Jorge / Pico do Areeiro (DROTRH)", "Madeira, Quinta Grande", "Madeira, Ponta do Sol", "Prazeres (Calheta)", "Madeira, Calheta", "Alcochete / Campo Tiro", "Mealhada / Quinta do Vale (CIM)", "P. Delgada (Obs. A. Chaves)", "São Miguel - Pico Santos De Cima", "Ilhas selvagens", "Pico (Aeródromo)", "Vila Verde (CIM)", "Mortágua / Aeródromo (CIM)", "Coruche / Cruz Do Leão", "Loulé / Cavalos De Caldeirão", "Oeiras / Vila Fria (CMO)", "Penacova / Hombres (CIM)", "Odemira, S.Teotónio", "Lisboa, Amoreiras (LFCL)", "Tondela, Caramulinho (CIM)", "Pico / Cabeço do Teicho (DROTRH)", "Horta (Obs. Principe Alberto)", "Graciosa (Aeródromo)", "V.N.Cerveira (Aeródromo)", "Monção, Valinha", "Lamas de Mouro, P.Ribeiro", "Montalegre", "Vinhais", "São Jorge", "Ponte de Lima", "Chaves (Aeródromo)", "Cabril", "Braga, Merelim", "Vila Nova de Poiares (CIM)", "Ponte de Sôr / Aeródromo", "Cabeceiras de Basto", "Mirandela", "Macedo Cavaleiros, Bagueixe", "Miranda do Douro", "Mogadouro", "Paços Ferreira", "Carrazêda de Ansiães", "Vouzela (CIM)", "S. Gens", "Faial / Cabeço Verde (DROTRH)", "Moncorvo", "Pinhão, Santa Bárbara", "Luzim", "Pico / Cabecinho (DROTRH)", "Moimenta da Beira", "Trancoso, Bandarra", "Arouca", "Faial / Alto do Cabouco (DROTRH)", "F. Castelo Rodrigo, V.Torpim", "Loulé (CML)", "Vila Nova Famalicão (CIM)", "Guarda", "Lisboa, Tapada da Ajuda", "Pampilhosa da Serra, Fajão", "Covilhã (Aeródromo)", "São Miguel / Santana (DROTRH)", "Aldeia Souto (Q. Lageosa)", "Porto, Massarelos", "Almada, P.Rainha", "Lousã (Aeródromo)", "Fundão", "Aveiro (Universidade)", "Dunas de Mira", "Anadia", "Coimbra, Bencanta", "Leiria", "Figueira da Foz, Vila Verde", "Mação/Cardigos (CIM)", "Ansião", "Leiria (Aeródromo)", "São Pedro de Moel", "São Miguel / Lagoa das Furnas (DROTRH)", "Tomar, Valdonas", "Alcobaça", "Amadora", "Rio Maior", "Santarém, Fonte Boa", "Nelas", "Torres Vedras, Dois Portos", "Flores (Aeródromo)", "Corvo (Aeródromo)", "Coruche", "Mação/Envendos(CIM)", "Santa Cruz (Aeródromo)", "Sintra, Colares", "Cabo da Roca", "Angra do Heroísmo", "P. Delgada (Aeródromo)", "Santa Maria (Aeródromo)", "Santa Catarina (Aeródromo)", "Funchal", "Madeira, Funchal, Lido", "Porto Santo", "Cabo Raso", "Barreiro, Lavradio", "Pegões", "Portimão (Praia da Rocha)", "Setúbal", "Cabo Carvoeiro", "Carregal do Sal (CIM)", "Sagres", "Mangualde / Chãs de Tavares  (CIM)", "Lisboa (Geofísico)", "Alcácer do Sal, Barrosinha", "Penalva do Castelo (CIM)", "São Pedro do Sul (CIM)", "Santa Comba Dão (CIM)", "Satão (CIM)", "Sines", "Vila Nova do Paiva (CIM)", "Alvalade", "Viseu / Torredeita (CIM)", "Porto, Pedras Rubras (Aeródromo)", "Coimbra (Aeródromo)", "Aljezur", "Fóia", "Viana Castelo, Chafé", "Faro (Aeródromo)", "Aguiar da Beira (CIM)", "Penela / Serra do Espinhal (CIM)", "Évora (C.Coordenação)", "Viseu (Aeródromo)", "Viseu (C.Coordenação)", "Beja", "Zebreira", "Proença-a-Nova, P.Moitas", "Vila Real", "Penhas Douradas", "Portalegre (cidade)", "Castelo Branco", "Portalegre", "Alvega", "Bragança", "Bragança (Aeródromo)", "Lisboa (G.Coutinho)", "Avis, Benavila", "Mora", "Elvas", "Estremoz", "Reguengos, S. P. do Corval", "Zambujeira", "Terceira / Ribeira das Nove (DROTRH)", "Terceira / Serra do Cume (DROTRH)", "São Miguel / Sete Cidades (DROTRH)", "Viana do Alentejo", "Portel, Oriola", "Porto, Serra do Pilar", "Amareleja", "Castro Daire / Mézio (CIM)", "Arganil / Aeródromo (CIM)", "Cantanhede / Fonte Dom Pedro (CIM)", "Coimbra / Mata de São Pedro (CIM)", "Góis / Quinta da Ribeira (CIM)", "Mértola, Vale Formoso", "Castro Verde, N.Corvo", "Alcoutim, Mart.Longo", "Vila Real de S.António", "Castro Marim (RN Sapal)", "Oliveira do Hospital (CIM)", "Soure (CIM)", "Vila Real (Cidade)", "Albufeira", "Portimão (Aeródromo)"));
        Widget temperatureWidget = Widget.builder().name("temperature").build();
        temperatureWidget.addVariable("station", WidgetVariableType.OPTION, stations);
        temperatureWidget = widgetRepository.save(temperatureWidget);

        // Weather Widget
        List<String> regions = new ArrayList<>(List.of("Aveiro", "Beja", "Braga", "Guimarães", "Bragança", "Castelo Branco", "Coimbra", "Évora", "Faro", "Sagres", "Portimão", "Loulé", "Guarda", "Penhas Douradas", "Leiria", "Lisboa", "Portalegre", "Porto", "Santarém", "Setúbal", "Sines", "Viana do Castelo", "Vila Real", "Viseu", "Funchal", "Porto Santo", "Vila do Porto", "Ponta Delgada", "Angra do Heroísmo", "Santa Cruz da Graciosa", "Velas", "Madalena", "Horta", "Santa Cruz das Flores", "Vila do Corvo"));
        Widget weatherWidget = Widget.builder().name("weather").build();
        weatherWidget.addVariable("region", WidgetVariableType.OPTION, regions);
        weatherWidget = widgetRepository.save(weatherWidget);    

        // Media Widget
        Widget mediaWidget = Widget.builder().name("media").build();
        mediaWidget.addVariable("videos", WidgetVariableType.MEDIA);
        mediaWidget = widgetRepository.save(mediaWidget); 

        // Time Widget
        Widget timeWidget = Widget.builder().name("time").build();
        timeWidget = widgetRepository.save(timeWidget); 

        // News Widget
        Widget newsWidget = Widget.builder().name("news").build();
        newsWidget = widgetRepository.save(newsWidget); 

        // News Widget
        Widget eventsWidget = Widget.builder().name("events").build();
        eventsWidget = widgetRepository.save(eventsWidget); 


        // Template 1
        Template template1 = Template.builder().name("Template1").build();

        Map<String, Object> defaultValuesTemperature = new HashMap<>();
        defaultValuesTemperature.put("station", "Aveiro (Universidade)");
        template1.addWidget(temperatureWidget, 0, 0, 20, 10, 1, defaultValuesTemperature);
        
        // video
        template1.addWidget(mediaWidget, 10, 20, 80, 80, 2);
        // logos
        template1.addWidget(mediaWidget, 0, 20, 11.5364f, 10, 6);
        template1.addWidget(mediaWidget, 90, 94.9235f, 5.0765f, 10, 7);
        template1.addWidget(mediaWidget, 0, 31.5364f, 27.3958f, 10, 8);
        
        template1.addWidget(timeWidget, 0, 80, 20, 10, 3);
        template1.addWidget(newsWidget, 90, 0, 94.9235f, 10, 4);
        template1.addWidget(eventsWidget, 10, 0, 20, 80, 5);
        template1 = templateRepository.save(template1);
    
        // // create template2
        // Template template2 = new Template();
        // template2.setName("template2");
        // templateRepository.save(template2);

        // // create the widget mappings for template 2
        // temperature = new TemplateWidget();
        // temperature.setZIndex(1);
        // temperature.setTop(0);
        // temperature.setLeftPosition(80);
        // temperature.setHeight(20);
        // temperature.setWidth(20);
        // temperature.setTemplate(template2);
        // temperature.setWidget(temperatureWidget);
        // templateWidgetRepository.save(temperature);

        // video = new TemplateWidget();
        // video.setZIndex(2);
        // video.setTop(0);
        // video.setLeftPosition(0);
        // video.setHeight(90);
        // video.setWidth(80);
        // video.setTemplate(template2);
        // video.setWidget(mediaWidget);
        // templateWidgetRepository.save(video);

        // news = new TemplateWidget();
        // news.setZIndex(3);
        // news.setTop(90);
        // news.setLeftPosition(0);
        // news.setHeight(10);
        // news.setWidth(100);
        // news.setTemplate(template2);
        // news.setWidget(newsWidget);
        // templateWidgetRepository.save(news);

        // events = new TemplateWidget();
        // events.setZIndex(4);
        // events.setTop(20);
        // events.setLeftPosition(80);
        // events.setHeight(70);
        // events.setWidth(20);
        // events.setTemplate(template2);
        // events.setWidget(eventsWidget);
        // templateWidgetRepository.save(events);
    }
}
