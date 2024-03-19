package deti.uas.uasmartsignage.Services;

import deti.uas.uasmartsignage.Models.File;
import deti.uas.uasmartsignage.Repositories.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class FileService {

    private final FileRepository fileRepository;

    @Autowired
    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public static String getUploadDir(File file) {
        StringBuilder pathBuilder = new StringBuilder();

        // Traverse the parent hierarchy until it becomes null (reaching the root directory)
        while (file.getParent() != null) {
            pathBuilder.insert(0, file.getParent().getName() + "/");
            file = file.getParent();
        }

        // Append a file separator for the root directory
        pathBuilder.insert(0, "/");

        return pathBuilder.toString();
    }


    public List<File> getAllFiles() {
        return fileRepository.findAll();
    }

    public File getFileById(Long id) {
        Optional<File> fileOptional = fileRepository.findById(id);
        if (fileOptional.isPresent()) {
            return fileOptional.get();
        } else {
            throw new RuntimeException("File not found with id " + id);
        }
    }

    public File createFile(File file) {
        return fileRepository.save(file);
    }

    public File updateFile(Long id, File file) {
        Optional<File> existingFileOptional = fileRepository.findById(id);
        if (existingFileOptional.isPresent()) {
            File existingFile = existingFileOptional.get();
            existingFile.setName(file.getName());
            return fileRepository.save(existingFile);
        } else {
            throw new RuntimeException("File not found with id " + id);
        }
    }

    public void deleteFile(Long id) {
        Optional<File> fileOptional = fileRepository.findById(id);
        if (fileOptional.isPresent()) {
            fileRepository.delete(fileOptional.get());
        } else {
            throw new RuntimeException("File not found with id " + id);
        }
    }



}
