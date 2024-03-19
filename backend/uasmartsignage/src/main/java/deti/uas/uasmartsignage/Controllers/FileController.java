package deti.uas.uasmartsignage.Controllers;

import deti.uas.uasmartsignage.Models.FilesClass;
import deti.uas.uasmartsignage.Models.File;
import deti.uas.uasmartsignage.Services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    //isto é para testar o upload de ficheiros
    //para dar upload é preciso usar forms ou equivalente com os argumentos em FilesClass (exemplo no html do form)
    @PostMapping
    public ResponseEntity<?> createFile(@ModelAttribute FilesClass file) {
        //remover name e type do fileclass (deve vir do file em si)
        //falta dar save ao file
        //System.out.println(file.toString());
        if (file.getFile().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        String fileName = StringUtils.cleanPath(file.getFile().getOriginalFilename());
        //System.out.println(fileName); working

        Long parentId = file.getParent().getId();

        File parent = fileService.getFileById(parentId);

        File newFile = new File(file.getName(), file.getType(), parent, file.getSubDirectories());

        File final_file = fileService.createFile(newFile);

        String uploadDir = FileService.getUploadDir(final_file);
        Path path = Paths.get(FileService.getUploadDir(newFile) + fileName);
        return new ResponseEntity<>(uploadDir, HttpStatus.OK);
        /*
        try {
            Files.copy(file.getFile().getInputStream(), path);
            return new ResponseEntity<>(uploadDir, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        */




    }

    @PostMapping("/test2")
    public ResponseEntity<File> createFile( @RequestBody File file) {
        File newFile = fileService.createFile(file);
        return new ResponseEntity<>(newFile, HttpStatus.CREATED);
    }

    @GetMapping("/test")
    public ResponseEntity<?> getFileUploadDir(@RequestBody File file) {
        String uploadDir = FileService.getUploadDir(file);
        return new ResponseEntity<>(uploadDir, HttpStatus.OK);
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
