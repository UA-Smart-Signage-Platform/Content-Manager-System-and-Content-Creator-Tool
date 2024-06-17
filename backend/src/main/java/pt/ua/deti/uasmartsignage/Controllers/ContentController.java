package pt.ua.deti.uasmartsignage.Controllers;

import pt.ua.deti.uasmartsignage.Models.Content;
import pt.ua.deti.uasmartsignage.Services.ContentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("content")
public class ContentController {

    private ContentService contentService;

    @Operation(summary = "Save content")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Content saved", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"))
    })
    @PostMapping
    public ResponseEntity<Content> saveContent(@RequestBody Content content) {
        Content savedContent = contentService.saveContent(content);
        return new ResponseEntity<>(savedContent, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all contents")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of all contents", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "No contents found", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"))
    })
    @GetMapping
    public ResponseEntity<List<Content>> getAllContents() {
        List<Content> contents = (List<Content>) contentService.getAllContents();
        return new ResponseEntity<>(contents, HttpStatus.OK);
    }

    @Operation(summary = "Get content by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Content found", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Content not found", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Content> getContentById(@PathVariable("id") Long id) {
        Content content = contentService.getContentById(id);
        if (content == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(content, HttpStatus.OK);
    }

    @Operation(summary = "Delete content by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Content deleted", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Content not found", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContent(@PathVariable("id") Long id) {
        contentService.deleteContent(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Update content by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Content updated", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Content not found", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json"))
    })
    @PutMapping("/{id}")
    public ResponseEntity<Content> updateContent(@PathVariable("id") Long id, @RequestBody Content content) {
        Content updatedContent = contentService.updateContent(id, content);
        if (updatedContent == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedContent, HttpStatus.OK);
    }
}
