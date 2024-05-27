package deti.uas.uasmartsignage.serviceTests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Optional;

import deti.uas.uasmartsignage.Services.LogsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import deti.uas.uasmartsignage.Models.Content;
import deti.uas.uasmartsignage.Models.Widget;
import deti.uas.uasmartsignage.Repositories.ContentRepository;
import deti.uas.uasmartsignage.Services.ContentService;

@ExtendWith(MockitoExtension.class)
class ContentServiceTest {

        @Mock
        private ContentRepository repository;

        @Mock
        private LogsService logsService;

        @InjectMocks
        private ContentService service;


        @Test
        void whenFindById_thenReturnContent() {
            Content content = new Content();
            content.setName("New Content");
            content.setType("type");
            content.setWidget(null);
            content.setOptions(new ArrayList<>());
            when(repository.findById(1L)).thenReturn(Optional.of(content));

            Content found = service.getContentById(1L);

            assertThat(found).isEqualTo(content);
            verify(repository, times(1)).findById(1L);
        }

        @Test
        void whenSaveContent_thenReturnContent() {
            Content content = new Content();
            content.setName("New Content");
            content.setType("type");
            content.setWidget(null);
            content.setOptions(new ArrayList<>());
            when(repository.save(content)).thenReturn(content);

            Content savedcontent = service.saveContent(content);

            assertThat(savedcontent).isEqualTo(content);
        }

        @Test
        void whenDeleteContent_thenVerifyRepositoryDelete() {
            Content content = new Content();
            content.setName("New Content");
            content.setType("type");
            content.setWidget(null);
            content.setOptions(new ArrayList<>());
            service.deleteContent(1L);
            verify(repository, times(1)).deleteById(1L);
            assertFalse(repository.existsById(1L));
        }

        @Test
        void whenUpdateContent_thenReturnUpdatedContent() {
                Widget widget = new Widget();
                widget.setName("widget");

                Widget widgetUpdated = new Widget();
                widgetUpdated.setName("widget1");

                Content content = new Content();
                content.setName("New Content");
                content.setType("type");
                content.setWidget(widget);
                content.setOptions(new ArrayList<>());

                Content contentUpdated = new Content();
                contentUpdated.setName("Updated Content");
                contentUpdated.setType("type1");
                contentUpdated.setWidget(widgetUpdated);
                contentUpdated.setOptions(new ArrayList<>());

                when(repository.save(content)).thenReturn(content);
                when(repository.findById(1L)).thenReturn(Optional.of(content));

                Content update = service.updateContent(1L, contentUpdated);

                assertThat(update.getName()).isEqualTo("Updated Content");
                assertThat(update.getType()).isEqualTo("type1");
        }

        @Test
        void testFindByAllContent() {
            Content content = new Content();
            content.setName("New Content");
            content.setType("type");
            content.setWidget(null);
            content.setOptions(new ArrayList<>());

            Content content2 = new Content();
            content2.setName("New Content2");
            content2.setType("type2");
            content2.setWidget(null);
            content2.setOptions(new ArrayList<>());

            when(repository.findAll()).thenReturn(java.util.Arrays.asList(content, content2));

            Iterable<Content> contents = service.getAllContents();

            assertThat(contents).hasSize(2).contains(content, content2);
        }
}
