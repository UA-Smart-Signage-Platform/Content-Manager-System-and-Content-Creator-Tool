package deti.uas.uasmartsignage.Services;

import deti.uas.uasmartsignage.Models.CustomFile;
import deti.uas.uasmartsignage.Models.FilesClass;
import deti.uas.uasmartsignage.Repositories.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class FileService {

    private static final Logger logger = LoggerFactory.getLogger(FileService.class);
    
    private final FileRepository fileRepository;

    @Autowired
    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }


    /**
     * Constructs and returns the parent directory path for the provided CustomFile.
     * The directory path is determined by traversing the parent directories of the CustomFile.
     * If the root directory for uploads does not exist, it will be created.
     *
     * @param customFile The CustomFile for which the parent directory path is generated.
     * @return The parent directory path for the given CustomFile.
     */
    public static String getParentDirectoryPath(CustomFile customFile) {
        StringBuilder pathBuilder = new StringBuilder();

        while (customFile.getParent() != null) {
            pathBuilder.insert(0, customFile.getParent().getName() + "/");
            customFile = customFile.getParent();
        }

        String rootPath = System.getProperty("user.dir");
        File rootDirectory = new File(rootPath + File.separator + "uploads");
        if (!rootDirectory.exists()) {
            if (rootDirectory.mkdir()) {
                logger.info("Directory created: {}", rootDirectory.getAbsolutePath());
            } 
            else {
                logger.error("Failed to create directory: {}", rootDirectory.getAbsolutePath());
            }
        }

        pathBuilder.insert(0, rootDirectory + File.separator);
        return pathBuilder.toString();
    }


    /**
     * Retrieves and returns a list of all CustomFile stored in the file repository.
     * 
     * @return A list of all CustomFile stored in the file repository.
     */
    public List<CustomFile> getAllFiles() {
        logger.info("Retrieving all files from the repository.");
        List<CustomFile> files = fileRepository.findAll();
        logger.debug("Retrieved {} files from the repository.", files.size());
        return files;
    }


    /**
     * Retrieves and returns CustomFile with the specified ID from the file repository.
     * 
     * @param id The ID of the CustomFile to retrieve.
     * @return The CustomFile with the specified ID, or null if no such file is found.
     */
    public CustomFile getFileById(Long id) {
        logger.info("Retrieving file with ID: {}", id);
        CustomFile file = fileRepository.findById(id).orElse(null);

        if (file == null) {
            logger.warn("File with ID {} not found", id);
        }
        return file;
    }


    /**
     * Retrieves and returns CustomFile with the specified name from the file repository.
     * 
     * @param fileName The name of the CustomFile to retrieve.
     * @return The CustomFile with the specified name, or {@code null} if no such file exists.
     */
    public CustomFile getFileByName(String fileName) {
        logger.info("Retrieving file with name: {}", fileName);
        CustomFile customFile = fileRepository.findByName(fileName);

        if (customFile == null) {
            logger.warn("File with name '{}' not found", fileName);
        }
        return customFile;
    }


    /**
     * Creates CustomFile in the repository. If the file type is "directory", a directory is created.
     *
     * @param customFile The CustomFile to create.
     * @return The created CustomFile, or {@code null} if creation fails.
     */
    public CustomFile createDirectory(CustomFile customFile) {
        if (!customFile.getType().equals("directory")) {
            return null;
        }

        String parentDirectoryPath = getParentDirectoryPath(customFile);
        File directory = new File(parentDirectoryPath + customFile.getName());

        if (directory.exists()){
            logger.info("Directory already exists: " + directory.getAbsolutePath());
            return null;
        }

        if (directory.mkdir()) {
            customFile.setPath(parentDirectoryPath + customFile.getName());
            fileRepository.save(customFile);
            logger.info("Directory created: " + directory.getAbsolutePath());
            return customFile;
        } 
        else {
            logger.info("Failed to create directory: " + directory.getAbsolutePath());
            return null;
        }
        
    }


    /**
     * Creates a new CustomFile from the provided FilesClass and saves it to the repository.
     * 
     * @param file The FilesClass containing information about the file to create (normally an image or video).
     * @return The created CustomFile, or {@code null} if creation fails.
     */
    public CustomFile createFile(FilesClass file) {
        if (file.getFile().isEmpty()) {
            return null;
        }
        
        // Get information from sent file
        String fileName = StringUtils.cleanPath(file.getFile().getOriginalFilename());
        String fileType = file.getFile().getContentType();
        Long fileSize = file.getFile().getSize();

        // Get parent and transform FilesClass onto a CustomFile
        CustomFile parent = (file.getParent() != null) ? getFileById(file.getParent().getId()) : null;
        CustomFile customFile = new CustomFile(fileName, fileType, fileSize, parent, null);

        Path path = Paths.get(getParentDirectoryPath(customFile) + fileName);

        customFile.setPath(path.toString());
        logger.info("Creating file with type: " + customFile.getType() + " and name: " + customFile.getName());
        fileRepository.save(customFile);

        try {
            Files.copy(file.getFile().getInputStream(), path);
            return customFile;
        } 
        catch (IOException e) {
            logger.error("Failed to copy file: {}", e.getMessage());
            return null;
        }
    }

    // TODO - need to revise logic and alike...


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
            } 
            else {
                throw new RuntimeException("File not found with id " + id);
            }
    }

    public void deleteFile(Long id) {
        Optional<CustomFile> fileOptional = fileRepository.findById(id);
        if (fileOptional.isPresent()) {
            fileRepository.delete(fileOptional.get());
        } 
        else {
            throw new RuntimeException("File not found with id " + id);
        }
    }
}