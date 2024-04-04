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


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/files")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    // Get all files
    @GetMapping
    public ResponseEntity<List<CustomFile>> getAllFiles() {
        List<CustomFile> customFiles = fileService.getAllFiles();
        return new ResponseEntity<>(customFiles, HttpStatus.OK);
    }

    // Get file by id
    @GetMapping("/{id}")
    public ResponseEntity<CustomFile> getFileById(@PathVariable Long id) {
        CustomFile customFile = fileService.getFileById(id);
        return new ResponseEntity<>(customFile, HttpStatus.OK);
    }

    // Create a new file (nao pode ser um diretorio)
    //isto é para testar o upload de ficheiros
    //para dar upload é preciso usar forms ou equivalente com os argumentos em FilesClass (exemplo no html em resources/static)
    @PostMapping
    public ResponseEntity<?> createFile(@ModelAttribute FilesClass file) {
        CustomFile savedFile = fileService.createAndSaveFile(file);
        if (savedFile == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(savedFile, HttpStatus.CREATED);
    }

    // Create a new directory (nao pode ser um ficheiro)
    @PostMapping("/directory")
    public ResponseEntity<CustomFile> createFile(@RequestBody CustomFile customFile) {
        CustomFile newCustomFile = fileService.createFile(customFile);
        if (newCustomFile == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(newCustomFile, HttpStatus.CREATED);
    }

    // Update an existing file
    //falta apagar o ficheiro do disco
    @PutMapping("/{id}")
    public ResponseEntity<CustomFile> updateFile(@PathVariable Long id, @RequestBody CustomFile customFile) {
        CustomFile updatedCustomFile = fileService.updateFile(id, customFile);
        return new ResponseEntity<>(updatedCustomFile, HttpStatus.OK);
    }

    // Delete a file
    //falta apagar o ficheiro do disco
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long id) {
        fileService.deleteFile(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Download file
    // not working needs fix
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

    //specific directory files (maybe useless ask frontend team)

}
