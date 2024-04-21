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
        this.loadGroupsAndMonitors();
  
    }

    private void loadGroupsAndMonitors(){

        MonitorsGroup deti = new MonitorsGroup();
        deti.setName("DETI");
        deti.setDescription("Monitors from first floor, second and third");
        deti.setMonitors(List.of());

        MonitorsGroup dMat = new MonitorsGroup();
        dMat.setName("DMAT");
        dMat.setDescription("Monitors from first floor and bar");
        dMat.setMonitors(List.of());

        MonitorsGroup dBio = new MonitorsGroup();
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

    private void loadTemplates() {

        // create the widgets and their contents
        Widget temperatureWidget = new Widget();
        temperatureWidget.setName("Temperature");
        temperatureWidget.setPath("static/widgets/temperature.widget");
        temperatureWidget.setContents(new ArrayList<>());
        widgetRepository.save(temperatureWidget);
        Content temperatureWidgetContent = new Content();
        temperatureWidgetContent.setName("station");
        temperatureWidgetContent.setType("options");
        List<String> stations = new ArrayList<>(List.of("Olhão, EPPO", "Tavira", "Graciosa / Serra das Fontes (DROTRH)", "Terras de Bouro/Barral (CIM)", "Amares Caldelas (CIM)", "Faja / Ilha das Flores", "Braga (CIM)", "Barcelos (CIM)", "Esposende (CIM)", "Sabugal, Martim Rei", "Viana do Castelo", "Nelas / Vilar Seco (CIM)", "Santa Maria / Praia Formosa (DROTRH)", "S. Miguel, Nordeste", "Oliveira de Frades (CIM)", "Setúbal, Areias", "Madeira, Porto Moniz", "Santa Maria / Maia (DROTRH)", "Madeira, S. Jorge, Santana", "Madeira, Santana", "Mação (CIM)", "Madeira, São Vicente", "Madeira, Bica da Cana", "Madeira, Pico Alto", "Madeira, Areeiro", "Madeira, Pico do Areeiro", "Madeira, Santo da Serra", "Madeira, Monte", "Madeira, Cancela", "Madeira, S. Lourenço", "Viseu (Cidade)", "Madeira, Lombo da Terça", "São Jorge / Pico do Areeiro (DROTRH)", "Madeira, Quinta Grande", "Madeira, Ponta do Sol", "Prazeres (Calheta)", "Madeira, Calheta", "Alcochete / Campo Tiro", "Mealhada / Quinta do Vale (CIM)", "P. Delgada (Obs. A. Chaves)", "São Miguel - Pico Santos De Cima", "Ilhas selvagens", "Pico (Aeródromo)", "Vila Verde (CIM)", "Mortágua / Aeródromo (CIM)", "Coruche / Cruz Do Leão", "Loulé / Cavalos De Caldeirão", "Oeiras / Vila Fria (CMO)", "Penacova / Hombres (CIM)", "Odemira, S.Teotónio", "Lisboa, Amoreiras (LFCL)", "Tondela, Caramulinho (CIM)", "Pico / Cabeço do Teicho (DROTRH)", "Horta (Obs. Principe Alberto)", "Graciosa (Aeródromo)", "V.N.Cerveira (Aeródromo)", "Monção, Valinha", "Lamas de Mouro, P.Ribeiro", "Montalegre", "Vinhais", "São Jorge", "Ponte de Lima", "Chaves (Aeródromo)", "Cabril", "Braga, Merelim", "Vila Nova de Poiares (CIM)", "Ponte de Sôr / Aeródromo", "Cabeceiras de Basto", "Mirandela", "Macedo Cavaleiros, Bagueixe", "Miranda do Douro", "Mogadouro", "Paços Ferreira", "Carrazêda de Ansiães", "Vouzela (CIM)", "S. Gens", "Faial / Cabeço Verde (DROTRH)", "Moncorvo", "Pinhão, Santa Bárbara", "Luzim", "Pico / Cabecinho (DROTRH)", "Moimenta da Beira", "Trancoso, Bandarra", "Arouca", "Faial / Alto do Cabouco (DROTRH)", "F. Castelo Rodrigo, V.Torpim", "Loulé (CML)", "Vila Nova Famalicão (CIM)", "Guarda", "Lisboa, Tapada da Ajuda", "Pampilhosa da Serra, Fajão", "Covilhã (Aeródromo)", "São Miguel / Santana (DROTRH)", "Aldeia Souto (Q. Lageosa)", "Porto, Massarelos", "Almada, P.Rainha", "Lousã (Aeródromo)", "Fundão", "Aveiro (Universidade)", "Dunas de Mira", "Anadia", "Coimbra, Bencanta", "Leiria", "Figueira da Foz, Vila Verde", "Mação/Cardigos (CIM)", "Ansião", "Leiria (Aeródromo)", "São Pedro de Moel", "São Miguel / Lagoa das Furnas (DROTRH)", "Tomar, Valdonas", "Alcobaça", "Amadora", "Rio Maior", "Santarém, Fonte Boa", "Nelas", "Torres Vedras, Dois Portos", "Flores (Aeródromo)", "Corvo (Aeródromo)", "Coruche", "Mação/Envendos(CIM)", "Santa Cruz (Aeródromo)", "Sintra, Colares", "Cabo da Roca", "Angra do Heroísmo", "P. Delgada (Aeródromo)", "Santa Maria (Aeródromo)", "Santa Catarina (Aeródromo)", "Funchal", "Madeira, Funchal, Lido", "Porto Santo", "Cabo Raso", "Barreiro, Lavradio", "Pegões", "Portimão (Praia da Rocha)", "Setúbal", "Cabo Carvoeiro", "Carregal do Sal (CIM)", "Sagres", "Mangualde / Chãs de Tavares  (CIM)", "Lisboa (Geofísico)", "Alcácer do Sal, Barrosinha", "Penalva do Castelo (CIM)", "São Pedro do Sul (CIM)", "Santa Comba Dão (CIM)", "Satão (CIM)", "Sines", "Vila Nova do Paiva (CIM)", "Alvalade", "Viseu / Torredeita (CIM)", "Porto, Pedras Rubras (Aeródromo)", "Coimbra (Aeródromo)", "Aljezur", "Fóia", "Viana Castelo, Chafé", "Faro (Aeródromo)", "Aguiar da Beira (CIM)", "Penela / Serra do Espinhal (CIM)", "Évora (C.Coordenação)", "Viseu (Aeródromo)", "Viseu (C.Coordenação)", "Beja", "Zebreira", "Proença-a-Nova, P.Moitas", "Vila Real", "Penhas Douradas", "Portalegre (cidade)", "Castelo Branco", "Portalegre", "Alvega", "Bragança", "Bragança (Aeródromo)", "Lisboa (G.Coutinho)", "Avis, Benavila", "Mora", "Elvas", "Estremoz", "Reguengos, S. P. do Corval", "Zambujeira", "Terceira / Ribeira das Nove (DROTRH)", "Terceira / Serra do Cume (DROTRH)", "São Miguel / Sete Cidades (DROTRH)", "Viana do Alentejo", "Portel, Oriola", "Porto, Serra do Pilar", "Amareleja", "Castro Daire / Mézio (CIM)", "Arganil / Aeródromo (CIM)", "Cantanhede / Fonte Dom Pedro (CIM)", "Coimbra / Mata de São Pedro (CIM)", "Góis / Quinta da Ribeira (CIM)", "Mértola, Vale Formoso", "Castro Verde, N.Corvo", "Alcoutim, Mart.Longo", "Vila Real de S.António", "Castro Marim (RN Sapal)", "Oliveira do Hospital (CIM)", "Soure (CIM)", "Vila Real (Cidade)", "Albufeira", "Portimão (Aeródromo)"));
        temperatureWidgetContent.setOptions(stations);
        temperatureWidgetContent.setWidget(temperatureWidget);
        contentRepository.save(temperatureWidgetContent);

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
        temperature.setLeftPosition(80);
        temperature.setHeight(20);
        temperature.setWidth(20);
        temperature.setTemplate(template2);
        temperature.setWidget(temperatureWidget);
        templateWidgetRepository.save(temperature);

        video = new TemplateWidget();
        video.setTop(0);
        video.setLeftPosition(0);
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
        image.setLeftPosition(80);
        image.setHeight(70);
        image.setWidth(20);
        image.setTemplate(template2);
        image.setWidget(imageWidget);
        templateWidgetRepository.save(image);
    }
}
