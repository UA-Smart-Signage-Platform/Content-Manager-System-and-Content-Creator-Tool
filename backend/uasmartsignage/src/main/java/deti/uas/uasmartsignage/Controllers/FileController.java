package deti.uas.uasmartsignage.Controllers;

import deti.uas.uasmartsignage.Models.File;
import deti.uas.uasmartsignage.Services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/files")
public class FileController {

    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    // Get all files
    @GetMapping
    public ResponseEntity<List<File>> getAllFiles() {
        List<File> files = fileService.getAllFiles();
        return new ResponseEntity<>(files, HttpStatus.OK);
    }

    // Get file by id
    @GetMapping("/{id}")
    public ResponseEntity<File> getFileById(@PathVariable Long id) {
        File file = fileService.getFileById(id);
        return new ResponseEntity<>(file, HttpStatus.OK);
    }

    // Create a new file
    @PostMapping
    public ResponseEntity<File> createFile(@RequestBody File file) {
        File newFile = fileService.createFile(file);
        return new ResponseEntity<>(newFile, HttpStatus.CREATED);
    }

    // Update an existing file
    @PutMapping("/{id}")
    public ResponseEntity<File> updateFile(@PathVariable Long id, @RequestBody File file) {
        File updatedFile = fileService.updateFile(id, file);
        return new ResponseEntity<>(updatedFile, HttpStatus.OK);
    }

    // Delete a file
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long id) {
        fileService.deleteFile(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //specific directory files (maybe useless ask frontend team)

}
