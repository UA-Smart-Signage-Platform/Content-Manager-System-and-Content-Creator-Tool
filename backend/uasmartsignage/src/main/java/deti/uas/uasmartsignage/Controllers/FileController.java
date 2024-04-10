package deti.uas.uasmartsignage.Controllers;

import deti.uas.uasmartsignage.Models.CustomFile;
import deti.uas.uasmartsignage.Models.FilesClass;
import deti.uas.uasmartsignage.Services.FileService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @Operation(summary = "Get all files")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of all files", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "No files found", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/files")
    public ResponseEntity<List<CustomFile>> getAllFiles() {
        List<CustomFile> customFiles = fileService.getAllFiles();
        return new ResponseEntity<>(customFiles, HttpStatus.OK);
    }

    @Operation(summary = "Get file by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File found", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "File not found", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/files/{id}")
    public ResponseEntity<CustomFile> getFileById(@PathVariable Long id) {
        CustomFile customFile = fileService.getFileById(id);
        return new ResponseEntity<>(customFile, HttpStatus.OK);
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

    //falta apagar o ficheiro do disco
    @Operation(summary = "Update a file")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File updated", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "File not found", content = @Content(mediaType = "application/json"))
    })
    @PutMapping("/{id}")
    public ResponseEntity<CustomFile> updateFile(@PathVariable Long id, @RequestBody CustomFile customFile) {
        CustomFile updatedCustomFile = fileService.updateFile(id, customFile);
        return new ResponseEntity<>(updatedCustomFile, HttpStatus.OK);
    }

    //falta apagar o ficheiro do disco
    @Operation(summary = "Delete a file")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "File deleted", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "File not found", content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long id) {
        fileService.deleteFile(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(summary = "Download a file")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File downloaded", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "File not found", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) throws IOException {
        // Define the path to the directory containing the files
        CustomFile customFile = fileService.getFileByName(fileName);
        String sFilePath = customFile.getPath();

        // Resolve the path to the requested file
        Path filePath = Paths.get(sFilePath);

        // Create a Resource representing the file
        Resource fileResource = new UrlResource(filePath.toUri());

        // Check if the file exists
        if (fileResource.exists() && fileResource.isReadable()) {
            // Set up response headers
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileResource.getFilename() + "\"");

            // Return ResponseEntity with the file resource and headers
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(fileResource);
        } else {
            // If the file does not exist, return ResponseEntity with status 404 Not Found
            return ResponseEntity.notFound().build();
        }
    }

    /* @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId) {
        CustomFile file = fileService.getFileById(fileId);

        if (file == null) {
            return ResponseEntity.notFound().build();
        }

        Path filePath = Paths.get(file.getPath());
        if (!Files.exists(filePath)) {
            return ResponseEntity.notFound().build(); 
        }

        try {
            Resource resource = new UrlResource(filePath.toUri()); 

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(Files.size(filePath)) 
                    .body(resource);
        } catch (IOException ex) {
            // Log the error 
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    } */

}