package pt.ua.deti.uasmartsignage.services;


import pt.ua.deti.uasmartsignage.enums.Description;
import pt.ua.deti.uasmartsignage.enums.Log;
import pt.ua.deti.uasmartsignage.enums.Operation;
import pt.ua.deti.uasmartsignage.enums.Severity;
import pt.ua.deti.uasmartsignage.models.CustomFile;
import pt.ua.deti.uasmartsignage.models.embedded.FilesClass;
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

    private final String defaultFolder = "defaultFolder";


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
            logger.error(Log.ERROR.toString());
        }
        else {
            logger.info(Log.SUCCESS.toString(), description);
        }
            
        return files;
    }


    /**
     * Retrieves and returns CustomFile with the specified ID from the file repository.
     * 
     * @param id The ID of the CustomFile to retrieve.
     * @return The CustomFile with the specified ID, or null if no such file is found.
     */
    public Optional<CustomFile> getFileById(Long id) {
        logger.info("Retrieving file with ID: {}", id);
        Optional<CustomFile> file = fileRepository.findById(id);
        if (file.isEmpty()) {
            logger.warn(Log.FILENOTFOUND.toString(), id);
            return Optional.empty();
        }
        else {
            logger.info("File with ID {} found", id);
        }

        // Add log to InfluxDB
        String operation = "getFileOrDirectoryById";
        String description = "Retrieved file with ID: " + id;
        if (!logsService.addBackendLog(Severity.INFO, source, operation, description)) {
            logger.error(Log.ERROR.toString());
        }
        else {
            logger.info(Log.SUCCESS.toString(), description);
        }
        
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

        File directory = new File(Log.USERDIR.toString() + "/uploads/" + customFile.getUuid());

        String operation = "createDirectory";
        String description = "Directory created: " + directory.getAbsolutePath();

        if (directory.mkdir()) {
            customFile.setSubDirectories(new ArrayList<>());
            CustomFile savedFile = fileRepository.save(customFile);
            if (!logsService.addBackendLog(Severity.INFO, source, operation, description)) {
                logger.error(Log.ERROR.toString());
            }
            else {
                logger.info(Log.SUCCESS.toString(), description);
            }
            return savedFile;
        }
        else {
            logger.info("Failed to create directory: {}",directory.getAbsolutePath());
            return null;
        }
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
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getFile().getOriginalFilename()).replaceFirst("[.][^.]+$", ""));
        String[] fileContentType= file.getFile().getContentType().split("/");
        String fileType = fileContentType[0];
        String fileExtension = fileContentType[1];
        String fileUuid = UUID.randomUUID().toString();
        Long fileSize = file.getFile().getSize();

        Optional<CustomFile> parent = (file.getParentId() != null) ? getFileById(file.getParentId()) : Optional.empty();
        if (parent.isEmpty()) {
            customFile = new CustomFile(fileName, fileUuid, fileType, fileExtension, fileSize, null);
        }
        else {
            customFile = new CustomFile(fileName, fileUuid, fileType, fileExtension, fileSize, parent.get());
            updateParentSize(parent, fileSize);
        }

        Path fileSystemPath = Paths.get(Log.USERDIR.toString() + "/uploads/" + customFile.getUuid() + "." + customFile.getExtension());
        String operation = "createFile";
        String description = "File created: " + fileSystemPath;
        
        try {
            Files.copy(file.getFile().getInputStream(), fileSystemPath);
            fileRepository.save(customFile);
            addLogEntry(operation, description);
            return customFile;
        } 
        catch (IOException e) {
            logger.error("Failed to save file: {}", e.getMessage());
            return null;
        }
    }

    
    /**
     * Updates parent size and propagates size to upper folders.
     *
     * @param parent The parent to be updated.
     * @param fileSize The size of the newly added file to be added to the parent's size
     */
    private void updateParentSize(Optional<CustomFile> parent, Long fileSize) {
        if (parent.isPresent()) {
            CustomFile currentParent = parent.get();
            currentParent.setSize(currentParent.getSize() + fileSize);
            fileRepository.save(currentParent);
            
            while(currentParent.getParent() != null){
                currentParent = currentParent.getParent();
                currentParent.setSize(currentParent.getSize() + fileSize);
                fileRepository.save(currentParent);
            }
        }
    }


    /**
     * TODO
     *
     * @param operation TODO
     * @param description TODO
     */
    private void addLogEntry(String operation, String description) {
        if (!logsService.addBackendLog(Severity.INFO, source, operation, description)) {
            logger.error(Log.ERROR.toString());
        } else {
            logger.info(Log.SUCCESS.toString(), description);
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
            logger.warn(Log.FILENOTFOUND.toString(), id);
            return false;
        }

        // Delete file from disk
        CustomFile file = fileOptional.get();
        String filePath =  Log.USERDIR.toString() + "/uploads/" + file.getUuid() + "." + file.getExtension();

        if (file.getType().equals("directory")) {
            
        } 
        else {
            try {
                Files.deleteIfExists(Paths.get(filePath));
            } 
            catch (IOException e) {
                logger.error("Failed to delete file: {}, error: {}", filePath, e.getMessage());
                return false;
            }
        }

        // Delete file from repository
        CustomFile parent = file.getParent();
        fileRepository.delete(file);

        if (parent != null){
            parent.setSize(parent.getSize() - file.getSize());
            fileRepository.save(parent);
        }

        return true;
    }

    /**
     * Updates the name of the file with the specified ID.
     *
     * @param id The ID of the file to update.
     * @param fileName The string containing the new name.
     * @return The updated CustomFile, or {@code null} if the update fails.
     */
    public CustomFile updateFileName(Long id, String fileName) {
        Optional<CustomFile> file = fileRepository.findById(id);
        if (file.isEmpty()) {
            logger.warn(Log.FILENOTFOUND.toString(), id);
            return null;
        }

        CustomFile updatedFile = file.get();
        updatedFile.setName(fileName);

        return fileRepository.save(updatedFile);
    }

    /**
     * Downloads the file with the specified id.
     *
     * @param fileId The id of the file to download.
     * @return ResponseEntity with the file resource and headers.
     */
    public Resource downloadFileById(Long fileId) throws MalformedURLException {
        Optional<CustomFile> customFile = fileRepository.findById(fileId);
        if (customFile.isEmpty()) {
            return null;
        }

        Path filePath = Paths.get(Log.USERDIR.toString() + "/uploads/" + customFile.get().getUuid() + "." + customFile.get().getExtension());
        Resource fileResource = new UrlResource(filePath.toUri());

        
        if (fileResource.exists() && fileResource.isReadable()) {
            return fileResource;
        } 
        else {
            return null;
        }
    }
}