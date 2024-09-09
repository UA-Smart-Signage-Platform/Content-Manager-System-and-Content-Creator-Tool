package pt.ua.deti.uasmartsignage.controllers;

import pt.ua.deti.uasmartsignage.models.CustomFile;
import pt.ua.deti.uasmartsignage.models.embedded.FilesClass;
import pt.ua.deti.uasmartsignage.services.FileService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
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
@CrossOrigin(origins = "*")
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
    public ResponseEntity<Optional<CustomFile>> getFileById(@PathVariable Long id) {
        Optional<CustomFile> customFile = fileService.getFileById(id);
        if (customFile.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(customFile, HttpStatus.OK);
    }

    @Operation(summary = "Get all files and folders located in root")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of all files and folders at root level", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/files/directory/root")
    public ResponseEntity<List<CustomFile>> getRootFiles() {
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
        CustomFile savedFile = fileService.createDirectory(customFile);
        if (savedFile == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } 
        return new ResponseEntity<>(savedFile, HttpStatus.CREATED);
    }

    @Operation(summary = "Delete a file")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "File deleted", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "File not found", content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/files/{id}")
    public ResponseEntity<Boolean> deleteFile(@PathVariable Long id) {
        boolean deleted = fileService.deleteFile(id);
        if (!deleted) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } 
        return new ResponseEntity<>(deleted, HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Download a file")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "File found", content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "File not found", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/files/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId) throws MalformedURLException {
        Resource file = fileService.downloadFileById(fileId);

        // String operation = "downloadFileById";
        // String description = "File downloaded: " + filePath;
        // if (!logsService.addBackendLog(Severity.INFO, source, operation, description)) {
        //     logger.error(Log.ERROR.toString());
        // }
        // else {
        //     logger.info(Log.SUCCESS.toString(), description);
        // }

        if (file == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"");

        return ResponseEntity.ok().headers(headers).body(file);
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