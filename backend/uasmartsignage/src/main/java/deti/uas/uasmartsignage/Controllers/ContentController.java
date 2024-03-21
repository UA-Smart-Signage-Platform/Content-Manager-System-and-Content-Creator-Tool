package deti.uas.uasmartsignage.Controllers;

import deti.uas.uasmartsignage.Models.Content;
import deti.uas.uasmartsignage.Services.ContentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/content")
public class ContentController {

    private ContentService contentService;

    @PostMapping
    public ResponseEntity<?> saveContent(@RequestBody Content content) {
        Content savedContent = contentService.saveContent(content);
        return new ResponseEntity<>(savedContent, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> getAllContents() {
        List<Content> contents = (List<Content>) contentService.getAllContents();
        return new ResponseEntity<>(contents, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getContentById(@PathVariable("id") Long id) {
        Content content = contentService.getContentById(id);
        if (content == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(content, HttpStatus.OK);
    }
}
