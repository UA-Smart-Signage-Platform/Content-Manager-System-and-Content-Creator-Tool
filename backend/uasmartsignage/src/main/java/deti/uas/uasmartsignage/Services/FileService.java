package deti.uas.uasmartsignage.Services;

import deti.uas.uasmartsignage.Models.CustomFile;
import deti.uas.uasmartsignage.Models.FilesClass;
import deti.uas.uasmartsignage.Repositories.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.io.File;

@Service
public class FileService {

    private final FileRepository fileRepository;

    @Autowired
    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }


    // This method is used to get the upload directory for a file
    // if the upload directory does not exist, it will create it
    // and return the path to the directory
    public static String getUploadDir(CustomFile customFile) {
        StringBuilder pathBuilder = new StringBuilder();

        while (customFile.getParent() != null) {
            pathBuilder.insert(0, customFile.getParent().getName() + "/");
            customFile = customFile.getParent();
        }

        String rootDir = System.getProperty("user.dir");
        File rootDirFile = new File(rootDir + File.separator + "uploads");
        if (!rootDirFile.exists()) {
            if (rootDirFile.mkdir()) {
                System.out.println("Directory created: " + rootDirFile.getAbsolutePath());
            } else {
                System.out.println("Failed to create directory: " + rootDirFile.getAbsolutePath());
            }
        }

        pathBuilder.insert(0, rootDirFile + File.separator);

        return pathBuilder.toString();
    }


    public List<CustomFile> getAllFiles() {
        return fileRepository.findAll();
    }

    public CustomFile getFileById(Long id) {
        return fileRepository.findById(id).orElse(null);
    }

    public CustomFile createFile(CustomFile customFile) {
        //System.out.println(customFile.getType());
        if (customFile.getType().equals("directory")) {
            String Dir = getUploadDir(customFile);
            File directory = new File(Dir + customFile.getName());
            if (!directory.exists()){
                if (directory.mkdir()) {
                    fileRepository.save(customFile);
                    System.out.println("Directory created: " + directory.getAbsolutePath());
                    customFile.setPath(Dir + customFile.getName());
                    updateFile(customFile.getId(), customFile);
                    return customFile;

                } else {
                    System.out.println("Failed to create directory: " + directory.getAbsolutePath());
                    return null;
                }
            }
        }
        return fileRepository.save(customFile);
    }

    public CustomFile updateFile(Long id, CustomFile customFile) {
        Optional<CustomFile> fileOptional = fileRepository.findById(id);
            if (fileOptional.isPresent()) {
                CustomFile file = fileOptional.get();
                file.setName(customFile.getName());
                file.setType(customFile.getType());
                file.setParent(customFile.getParent());
                file.setPath(customFile.getPath());
                file.setSubDirectories(customFile.getSubDirectories());
                return fileRepository.save(file);
            } else {
                throw new RuntimeException("File not found with id " + id);
            }
    }

    public void deleteFile(Long id) {
        Optional<CustomFile> fileOptional = fileRepository.findById(id);
        if (fileOptional.isPresent()) {
            fileRepository.delete(fileOptional.get());
        } else {
            throw new RuntimeException("File not found with id " + id);
        }
    }

    public CustomFile createAndSaveFile(FilesClass file) {
        CustomFile newCustomFile;

        System.out.println(file);

        if (file.getFile().isEmpty()) {
            return null;
        }
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getFile().getOriginalFilename()));
        String fileType = file.getFile().getContentType();

        

        if (file.getParent() != null) {
            Long parentId = file.getParent().getId();
            CustomFile parent = getFileById(parentId);
            newCustomFile = new CustomFile(fileName, fileType, parent, file.getSubDirectories());
            System.out.println("Parent: " + newCustomFile.toString());
        }
        else{
            newCustomFile = new CustomFile(fileName, fileType, null, file.getSubDirectories());
        }

        CustomFile finalCustomFile = createFile(newCustomFile);

        Path path = Paths.get(getUploadDir(newCustomFile) + fileName);

        finalCustomFile.setPath(path.toString());
        updateFile(finalCustomFile.getId(), finalCustomFile);

        try {
            Files.copy(file.getFile().getInputStream(), path);
            return finalCustomFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public CustomFile getFileByName(String fileName) {
        return fileRepository.findByName(fileName);
    }
}
