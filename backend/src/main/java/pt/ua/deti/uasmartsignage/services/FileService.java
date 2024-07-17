package pt.ua.deti.uasmartsignage.services;


import pt.ua.deti.uasmartsignage.enums.Severity;
import pt.ua.deti.uasmartsignage.models.CustomFile;
import pt.ua.deti.uasmartsignage.models.FilesClass;
import pt.ua.deti.uasmartsignage.repositories.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.io.File;
import java.util.stream.Stream;

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
    private static final String FILENOTFOUND = "File with ID {} not found";
    private static final String USERDIR = System.getProperty("user.dir");


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
        else {
            logger.info(ADDLOGSUCCESS, description);
        }
            
        return files;
    }


    /**
     * Retrieves and returns CustomFile with the specified ID from the file repository.
     * 
     * @param id The ID of the CustomFile to retrieve.
     * @return The CustomFile with the specified ID, or null if no such file is found.
     */
    public Optional<CustomFile> getFileOrDirectoryById(Long id) {
        logger.info("Retrieving file with ID: {}", id);
        Optional<CustomFile> file = fileRepository.findById(id);
        if (file.isEmpty()) {
            logger.warn(FILENOTFOUND, id);
            return Optional.empty();
        }
        else {
            logger.info("File with ID {} found", id);
        }

        // Add log to InfluxDB
        String operation = "getFileOrDirectoryById";
        String description = "Retrieved file with ID: " + id;
        if (!logsService.addBackendLog(Severity.INFO, source, operation, description)) {
            logger.error(ADDLOGERROR);
        }
        else 
            logger.info(ADDLOGSUCCESS, description);
        return file;
    }

    /***
     * Retrieves and returns CustomFile with the specified Path from the file repository.
     * 
     * @param path The Path of the CustomFile to retrieve.
     * @return The CustomFile with the specified ID, or null if no such file is found.
     */
    public Optional<CustomFile> getFileOrDirectoryByPath(String path){
        path = path.replaceAll("[\n\r]", "_");
        logger.info("Retrieving file with path: {}", path);
        Optional<CustomFile> file = fileRepository.findByPath(path);
        if (file.isEmpty()) {
            logger.warn(FILENOTFOUND, path);
            return Optional.empty();
        } else {
            logger.info("File with Path {} found", path);
        }

        // Add log to InfluxDB
        String operation = "getFileOrDirectoryByPath";
        String description = "Retrieved file with path: " + path;
        if (!logsService.addBackendLog(Severity.INFO, source, operation, description)) {
            logger.error(ADDLOGERROR);
        }
        logger.info(ADDLOGSUCCESS, description);
        return file;

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

        //filesystem path
        StringBuilder pathBuilder = new StringBuilder();

        //database path
        String parentDirectoryPath = getParentDirectoryPath(customFile);
        pathBuilder.insert(0, parentDirectoryPath);
        pathBuilder.insert(0, USERDIR);

        File directory = new File(pathBuilder + customFile.getName());

        String operation = "createDirectory";
        String description = "Directory created: " + directory.getAbsolutePath();
        

        if (directory.exists()){
            logger.info("Directory already exists: {}", directory.getAbsolutePath());
            return null;
        }

        if (directory.mkdir()) {
            customFile.setPath(parentDirectoryPath + customFile.getName());
            customFile.setSubDirectories(new ArrayList<>());
            CustomFile savedFile = fileRepository.save(customFile);
            if (!logsService.addBackendLog(Severity.INFO, source, operation, description)) {
                logger.error(ADDLOGERROR);
            }
            else {
                logger.info(ADDLOGSUCCESS, description);
            }
            return savedFile;
        }
        else {
            logger.info("Failed to create directory: {}",directory.getAbsolutePath());
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

        
        if (customFile.getParent() != null){
            CustomFile parent = fileRepository.findById(customFile.getParent().getId()).orElse(null);
            pathBuilder.append(parent.getPath());
            pathBuilder.append("/");
        }
        else{
            pathBuilder.append("/uploads/");
        }

       

        //Creating upload dir
        File rootDirectory = new File(USERDIR + File.separator + "uploads");

        String operation = "getParentDirectoryPath";
        String description = "Root Directory created: " + rootDirectory.getAbsolutePath();
        String sDescription = "Parent Directory path: " + pathBuilder.toString();

        if (!rootDirectory.exists()) {
            if (rootDirectory.mkdir()) {
                if (!logsService.addBackendLog(Severity.INFO, source, operation, description)) {
                    logger.error(ADDLOGERROR);
                }
                else {
                    logger.info(ADDLOGSUCCESS, description);
                }
            } 
            else {
                logger.error("Failed to create directory: {}", rootDirectory.getAbsolutePath());
            }
        }

        if (!logsService.addBackendLog(Severity.INFO, source, operation, sDescription)) {
            logger.error(ADDLOGERROR);
        }
        else {
            logger.info(ADDLOGSUCCESS, sDescription);
        }

        //adding upload path
        return pathBuilder.toString();
    }


    /**
     * Creates a new CustomFile from the provided FilesClass and saves it to the repository.
     * 
     * @param file The FilesClass containing information about the file to create (normally an image or video).
     * @return The created CustomFile, or {@code null} if creation fails.
     */
    @Transactional
    public CustomFile createFile(FilesClass file) {
        CustomFile customFile;

        if (file.getFile().isEmpty()) {
            logger.info("Provided file is empty.");
            return null;
        }

        // Get information from uploaded file
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getFile().getOriginalFilename()));
        String fileType = file.getFile().getContentType();
        Long fileSize = file.getFile().getSize();

        // Get parent and transform FilesClass onto a CustomFile
        Optional<CustomFile> parent = (file.getParentId() != null) ? getFileOrDirectoryById(file.getParentId()) : Optional.empty();
        if (parent.isEmpty()) {
            logger.info("Parent directory not found (Going to root).");
            customFile = new CustomFile(fileName, fileType, fileSize, null);
        }
        else {
            //update parent directory
            logger.info("Parent directory found: {}", parent.get().getName());
            //add the new file size
            parent.get().setSize(parent.get().getSize() + fileSize);
            fileRepository.save(parent.get());
            customFile = new CustomFile(fileName, fileType, fileSize, parent.get());
        }


        // Creating file path for database
        Path path = Paths.get(getParentDirectoryPath(customFile) + fileName);
        customFile.setPath(path.toString());

        // Creating file path for filesystem
        StringBuilder pathBuilder = new StringBuilder();
        // Get parent path and add the filename
        String parentDirectoryPath = getParentDirectoryPath(customFile) + fileName;
        pathBuilder.insert(0, parentDirectoryPath);
        pathBuilder.insert(0, USERDIR);

        Path fileSysPath = Paths.get(pathBuilder.toString());

        String operation = "createFile";
        String description = "File created: " + fileSysPath;
        

        try {
            Files.copy(file.getFile().getInputStream(), fileSysPath);
            fileRepository.save(customFile);
            if (!logsService.addBackendLog(Severity.INFO, source, operation, description)) {
                logger.error(ADDLOGERROR);
            }
            else {
                logger.info(ADDLOGSUCCESS, description);
            }
            return customFile;
        } 
        catch (IOException e) {
            logger.error("Failed to save file: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Deletes the desired file from the repository and the filesystem.
     *
     * @param id The ID of the file to delete.
     * @return true if the file was successfully deleted, false otherwise.
     */
    public boolean deleteFile(Long id) {
        Optional<CustomFile> fileOptional = fileRepository.findById(id);
        if (fileOptional.isEmpty()) {
            logger.warn(FILENOTFOUND, id);
            return false;
        }

        // Delete file from disk
        CustomFile file = fileOptional.get();

        String filePath = USERDIR + file.getPath();
        File fileToDelete = new File(filePath);

        String operation = "deleteFile";
        String description = "File deleted: " + filePath;

        if (fileToDelete.isDirectory()) {
            if (!deleteDirectory(fileToDelete)) {
                logger.error("Failed to delete directory: {}", fileToDelete.getAbsolutePath());
                return false;
            }
        } else {
            try {
                Files.deleteIfExists(Paths.get(filePath));
            } catch (IOException e) {
                logger.error("Failed to delete file: {}, error: {}", filePath, e.getMessage());
                return false;
            }
        }

        // Delete file from repository
        fileRepository.delete(file);

        if (!logsService.addBackendLog(Severity.INFO, source, operation, description)) {
            logger.error(ADDLOGERROR);
        }
        else {
            logger.info(ADDLOGSUCCESS, description);
        }

        return true;
    }

    /**
     * Deletes the specified directory and all its contents.
     *
     * @param directory The directory to delete.
     * @return true if the directory was successfully deleted, false otherwise.
     */
    private boolean deleteDirectory(File directory) {
        Path directoryPath = directory.toPath();
        String operation = "deleteDirectory";
        String description = "Directory deleted: " + directory.getAbsolutePath();

        try (Stream<Path> paths = Files.walk(directoryPath)) {
            paths.sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);

            if (!logsService.addBackendLog(Severity.INFO, source, operation, description)) {
                logger.error(ADDLOGERROR);
            }
            else {
                logger.info(ADDLOGSUCCESS, description);
            }

            return true;
        } catch (IOException e) {
            logger.error("Failed to delete directory: {}, error: {}", directory.getAbsolutePath(), e.getMessage());
            return false;
        }
    }

    /**
     * Downloads the file with the specified id.
     *
     * @param fileId The id of the file to download.
     * @return ResponseEntity with the file resource and headers.
     */
    public ResponseEntity<Resource> downloadFileById(Long fileId) throws MalformedURLException {
        Optional<CustomFile> customFile = fileRepository.findById(fileId);
        if (customFile.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        String sFilePath = customFile.get().getPath();
        sFilePath = USERDIR + sFilePath;

        Path filePath = Paths.get(sFilePath);
        Resource fileResource = new UrlResource(filePath.toUri());

        String operation = "downloadFileById";
        String description = "File downloaded: " + filePath;

        if (fileResource.exists() && fileResource.isReadable()) {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileResource.getFilename() + "\"");

            if (!logsService.addBackendLog(Severity.INFO, source, operation, description)) {
                logger.error(ADDLOGERROR);
            }
            else {
                logger.info(ADDLOGSUCCESS, description);
            }

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(fileResource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Updates the name of the file with the specified ID.
     *
     * @param id The ID of the file to update.
     * @param customFile The CustomFile containing the new name.
     * @return The updated CustomFile, or {@code null} if the update fails.
     */
    public CustomFile updateFileName(Long id, CustomFile customFile) {
        Optional<CustomFile> file = fileRepository.findById(id);
        if (file.isEmpty()) {
            logger.warn(FILENOTFOUND, id);
            return null;
        }

        CustomFile updatedFile = file.get();
        updatedFile.setName(customFile.getName());

        String parentDirectoryPath = getParentDirectoryPath(updatedFile);

        File fileToUpdate = new File(updatedFile.getPath());
        File newFile = new File(parentDirectoryPath + customFile.getName());

        String operation = "updateFileName";
        String description = "File renamed: " + newFile.getAbsolutePath();

        if (fileToUpdate.renameTo(newFile)) {
            updatedFile.setPath(parentDirectoryPath + updatedFile.getName());
            fileRepository.save(updatedFile);
            if (!logsService.addBackendLog(Severity.INFO, source, operation, description)) {
                logger.error(ADDLOGERROR);
            }
            else {
                logger.info(ADDLOGSUCCESS, description);
            }
        }
        else {
            logger.error("Failed to rename file: {}", newFile.getAbsolutePath());
        }

        return updatedFile;
    }


}
