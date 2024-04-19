package deti.uas.uasmartsignage.Services;


import deti.uas.uasmartsignage.Models.CustomFile;
import deti.uas.uasmartsignage.Models.FilesClass;
import deti.uas.uasmartsignage.Models.Severity;
import deti.uas.uasmartsignage.Repositories.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class FileService {

    private static final Logger logger = LoggerFactory.getLogger(FileService.class);
    
    private final FileRepository fileRepository;

    private final LogsService logsService;

    private final String source = this.getClass().getSimpleName();

    private static final String ADDLOGERROR = "Failed to add log to InfluxDB";
    private static final String ADDLOGSUCCESS = "Added log to InfluxDB: {}";

    @Autowired
    public FileService(FileRepository fileRepository, LogsService logsService) {
        this.fileRepository = fileRepository;
        this.logsService = logsService;
    }

   
    /**
     * Retrieves and returns a list of all CustomFile stored at root level.
     * 
     * @return A list of all CustomFile stored at root level.
    */
    public List<CustomFile> getFilesAtRoot() {
        List<CustomFile> files = fileRepository.findAllByParentIsNull();
        logger.debug("Retrieved {} files and folders located at root level.", files.size());

        // Add log to InfluxDB
        String operation = "getFilesAtRoot";
        String description = "Retrieved files and folders located at root level";
        if (!logsService.addBackendLog(Severity.INFO, source, operation, description)) {
            logger.error(ADDLOGERROR);
        }

        logger.info(ADDLOGSUCCESS, description);
        return files;
    }


    /**
     * Retrieves and returns CustomFile with the specified ID from the file repository.
     * 
     * @param id The ID of the CustomFile to retrieve.
     * @return The CustomFile with the specified ID, or null if no such file is found.
     */
    public CustomFile getFileOrDirectoryById(Long id) {
        logger.info("Retrieving file with ID: {}", id);
        CustomFile file = fileRepository.findById(id).orElse(null);

        // Add log to InfluxDB
        String operation = "getFileOrDirectoryById";
        String description = "Retrieved file with ID: " + id;
        if (!logsService.addBackendLog(Severity.INFO, source, operation, description)) {
            logger.error(ADDLOGERROR);
        }

        if (file == null) {
            logger.warn("File with ID {} not found", id);
        }
        logger.info(ADDLOGSUCCESS, description);
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

        // Add log to InfluxDB
        String operation = "getFileByName";
        String description = "Retrieved file with name: " + fileName;
        if (!logsService.addBackendLog(Severity.INFO, source, operation, description)) {
            logger.error(ADDLOGERROR);
        }
        //does it make sense to log to influx if the file is not found?
        if (customFile == null) {
            logger.warn("File with name '{}' not found", fileName);
        }
        logger.info(ADDLOGSUCCESS, description);
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
            customFile.setSubDirectories(new ArrayList<>());
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
     * Constructs and returns the parent directory path for the provided CustomFile.
     * The directory path is determined by traversing the parent directories of the CustomFile.
     * If the root directory for uploads does not exist, it will be created.
     *
     * @param customFile The CustomFile for which the parent directory path is generated.
     * @return The parent directory path for the given CustomFile.
     */
    public String getParentDirectoryPath(CustomFile customFile) {
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
     * Creates a new CustomFile from the provided FilesClass and saves it to the repository.
     * 
     * @param file The FilesClass containing information about the file to create (normally an image or video).
     * @return The created CustomFile, or {@code null} if creation fails.
     */
    public CustomFile createFile(FilesClass file) {
        if (file.getFile().isEmpty()) {
            logger.info("Provided file is empty.");
            return null;
        }

        // Get information from sent file
        String fileName = StringUtils.cleanPath(file.getFile().getOriginalFilename());
        String fileType = file.getFile().getContentType();
        Long fileSize = file.getFile().getSize();

        // Get parent and transform FilesClass onto a CustomFile
        CustomFile parent = (file.getParentId() != null) ? getFileOrDirectoryById(file.getParentId()) : null;
        CustomFile customFile = new CustomFile(fileName, fileType, fileSize, parent);

        Path path = Paths.get(getParentDirectoryPath(customFile) + fileName);

        customFile.setPath(path.toString());
        logger.info("Creating file with type: " + customFile.getType() + " and name: " + customFile.getName());
        fileRepository.save(customFile);

        // Add file to parent's list
        if (parent != null){
            logger.info("Adding file to parent with path: " + customFile.getPath());
            List<CustomFile> subDirectories = parent.getSubDirectories();
            subDirectories.add(customFile);
            parent.setSubDirectories(subDirectories);
        }

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
