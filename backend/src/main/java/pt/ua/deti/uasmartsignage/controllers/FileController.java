package pt.ua.deti.uasmartsignage.controllers;

import pt.ua.deti.uasmartsignage.models.CustomFile;
import pt.ua.deti.uasmartsignage.models.FilesClass;
import pt.ua.deti.uasmartsignage.services.FileService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") //NOSONAR
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @Operation(summary = "Get file by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File found", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "File not found", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/files/{id}")
    public ResponseEntity<Optional<CustomFile>> getFileOrDirectoryById(@PathVariable Long id) {
        Optional<CustomFile> customFile = fileService.getFileOrDirectoryById(id);
        if (customFile.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(customFile);
    }

    @Operation(summary = "Get file by path")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File found", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "File not found", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/files/byPath")
    public ResponseEntity<Optional<CustomFile>> getFileOrDirectoryByPath(@RequestParam String path) {
        Optional<CustomFile> customFile = fileService.getFileOrDirectoryByPath(path);
        if (customFile.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(customFile);
    }


    @Operation(summary = "Get all files and folders located in root")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of all files and folders at root level", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/files/directory/root")
    public ResponseEntity<List<CustomFile>> getRootFilesAndDirectories() {
        List<CustomFile> customFiles = fileService.getFilesAtRoot();
        return new ResponseEntity<>(customFiles, HttpStatus.OK);
    }

    @Operation(summary = "Create a new file")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "File created", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/files")
    public ResponseEntity<?> createFile(@ModelAttribute FilesClass file) {
        CustomFile savedFile = fileService.createFile(file);
        if (savedFile == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(savedFile, HttpStatus.CREATED);
    }

    @Operation(summary = "Create a new directory")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Directory created", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/files/directory")
    public ResponseEntity<CustomFile> createDirectory(@RequestBody CustomFile customFile) {
        CustomFile newCustomFile = fileService.createDirectory(customFile);
        if (newCustomFile == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(newCustomFile, HttpStatus.CREATED);
    }

    @Operation(summary = "Delete a file")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "File deleted", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "File not found", content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/files/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long id) {
        boolean deleted = fileService.deleteFile(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Download a file")
    @GetMapping("/files/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId) throws MalformedURLException {
        return fileService.downloadFileById(fileId);
    }

    @Operation(summary = "Update a file")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File updated", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "File not found", content = @Content(mediaType = "application/json"))
    })
    @PutMapping("/files/{id}")
    public ResponseEntity<CustomFile> updateFile(@PathVariable Long id, @RequestBody String fileName) {
        CustomFile updatedFile = fileService.updateFileName(id, fileName);
        if (updatedFile == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updatedFile, HttpStatus.OK);
    }
}
