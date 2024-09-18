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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class FileService {

    private static final Logger logger = LoggerFactory.getLogger(FileService.class);
    
    private final FileRepository fileRepository;

    private final LogsService logsService;

    private final String source = this.getClass().getSimpleName();


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

        String operation = "getFilesAtRoot";
        String description = "Retrieved " + files.size() + " files and folders located at root level.";
        logsService.addLogEntry(Severity.INFO, source, operation, description, logger);
            
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

        String operation = "getFileOrDirectoryById";
        String description = "Retrieved file with ID: " + id;

        if (file.isEmpty()) {
            description = Log.FILENOTFOUND.format(id);
            logsService.addLogEntry(Severity.WARNING, source, operation, description, logger);
            return Optional.empty();
        }
        
        logsService.addLogEntry(Severity.INFO, source, operation, description, logger);
        
        return file;
    }


    /**
     * Creates CustomFile in the repository. If the file type is "directory", a directory is created.
     *
     * @param customFile The CustomFile to create.
     * @return The created CustomFile, or {@code null} if creation fails.
     */
    public CustomFile createDirectory(CustomFile customFile) {
        String operation = "createDirectory";
        String description = "Given file is not a directory! Execution ignored and terminated.";

        if (!customFile.getType().equals("directory")) {
            logsService.addLogEntry(Severity.WARNING, source, operation, description, logger);
            return null;
        }

        customFile.setUuid(UUID.randomUUID().toString());
        customFile.setExtension("");
        File directory = new File(Log.USERDIR.toString() + "/uploads/" + customFile.getUuid());

        if (directory.mkdir()) {
            customFile.setSubDirectories(new ArrayList<>());
            CustomFile savedFile = fileRepository.save(customFile);

            description = "Directory created: " + directory.getAbsolutePath();
            logsService.addLogEntry(Severity.INFO, source, operation, description, logger);

            return savedFile;
        }
        else {
            description = "Failed to create directory: " + directory.getAbsolutePath();
            logsService.addLogEntry(Severity.WARNING, source, operation, description, logger);
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
        String operation = "createFile";
        String description = "Given file is empty! Execution ignored and terminated.";

        if (file.getFile().isEmpty()) {
            logsService.addLogEntry(Severity.WARNING, source, operation, description, logger);
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
            updateParentSize(parent.get(), fileSize, false);
        }

        Path fileSystemPath = Paths.get(Log.USERDIR.toString() + "/uploads/" + customFile.getUuid() + "." + customFile.getExtension());

        description = "File created: " + fileSystemPath;
        
        try {
            Files.copy(file.getFile().getInputStream(), fileSystemPath);
            fileRepository.save(customFile);
            logsService.addLogEntry(Severity.INFO, source, operation, description, logger);
            return customFile;
        } 
        catch (IOException e) {
            description = "Failed to save file with name: " + fileName + "; Error message: " + e.getMessage();
            logsService.addLogEntry(Severity.WARNING, source, operation, description, logger);
            return null;
        }
    }

    
    /**
     * Updates parent size and propagates size to upper folders.
     *
     * @param parent The parent to be updated.
     * @param fileSize The size of the newly added file to be added to the parent's size
     * @param deletingFiles Boolean to decide wether we remove or add the size to the parent files
     */
    private void updateParentSize(CustomFile parent, Long fileSize, boolean deletingFiles) {
        CustomFile currentParent = parent;
        fileSize = deletingFiles ? -fileSize : fileSize;

        do {
            currentParent.setSize(currentParent.getSize() + fileSize);
            fileRepository.save(currentParent);

            currentParent = currentParent.getParent();
        } while (currentParent != null);
    }


    /**
     * Deletes the desired file from the repository and the filesystem.
     *
     * @param id The ID of the file to delete.
     * @return true if the file was successfully deleted, false otherwise.
     */
    public boolean deleteFile(Long id) {
        Optional<CustomFile> fileOptional = fileRepository.findById(id);
        String operation = "deleteFile";
        String description = "";

        if (fileOptional.isEmpty()) {
            description = Log.FILENOTFOUND.format(id);
            logsService.addLogEntry(Severity.WARNING, source, operation, description, logger);
            return false;
        }

        // Delete file from disk
        CustomFile file = fileOptional.get();

        if (file.getType().equals("directory")) {
            deleteChildren(file);
        } 
        else {
            String filePath =  Log.USERDIR.toString() + "/uploads/" + file.getUuid() + "." + file.getExtension();
            try {
                Files.deleteIfExists(Paths.get(filePath));
                description = "Deleted file: " + filePath;
                logsService.addLogEntry(Severity.INFO, source, operation, description, logger);
            } 
            catch (IOException e) {
                description = "Failed to delete file: " + filePath + " , error: " + e.getMessage();
                logsService.addLogEntry(Severity.WARNING, source, operation, description, logger);
                return false;
            }
        }

        // Delete file from repository
        CustomFile parent = file.getParent();
        fileRepository.delete(file);

        if (parent != null){
            updateParentSize(parent, file.getSize(), true);
        }

        return true;
    }

    /**
     * Deletes children from parent
     *
     * @param rootFile The parent file.
     * @return true if the file was successfully deleted, false otherwise.
     */
    private boolean deleteChildren(CustomFile rootFile) {
        String operation = "deleteChildren";
        String description = "";

        List<CustomFile> allFiles = new ArrayList<>();
        Queue<CustomFile> queue = new LinkedList<>();
        queue.add(rootFile);
    
        // Breadth-first traversal to collect all files and directories
        while (!queue.isEmpty()) {
            CustomFile file = queue.poll();
            allFiles.add(file);
            
            if (file.getType().equals("directory")) {
                queue.addAll(file.getSubDirectories());
            }
        }
    
        // Delete files and directories in reverse order (children before parents)
        for (int i = allFiles.size() - 1; i >= 0; i--) {
            CustomFile file = allFiles.get(i);
            String filePath = Log.USERDIR.toString() + "/uploads/" + file.getUuid();
            if(!file.getType().equals("directory")){
                filePath = filePath + "." + file.getExtension();
            }
            
            try {
                Files.deleteIfExists(Paths.get(filePath));
                description = "Deleted child: " + filePath;
                logsService.addLogEntry(Severity.INFO, source, operation, description, logger);
            } 
            catch (IOException e) {
                description = "Failed to delete child: " + filePath + "; error: " + e.getMessage();
                logsService.addLogEntry(Severity.WARNING, source, operation, description, logger);
                return false;
            }
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
        String operation = "updateFileName";
        String description = "";

        Optional<CustomFile> file = fileRepository.findById(id);
        if (file.isEmpty()) {
            description = Log.FILENOTFOUND.format(id);
            logsService.addLogEntry(Severity.WARNING, source, operation, description, logger);
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
        String operation = "downloadFileById";
        String description = "";

        Optional<CustomFile> customFile = fileRepository.findById(fileId);
        if (customFile.isEmpty()) {
            description = Log.FILENOTFOUND.format(fileId);
            logsService.addLogEntry(Severity.WARNING, source, operation, description, logger);
            return null;
        }

        Path filePath = Paths.get(Log.USERDIR.toString() + "/uploads/" + customFile.get().getUuid() + "." + customFile.get().getExtension());
        Resource fileResource = new UrlResource(filePath.toUri());

        
        if (fileResource.exists() && fileResource.isReadable()) {
            description = "Downloading file with ID: " + fileId;
            logsService.addLogEntry(Severity.INFO, source, operation, description, logger);
            return fileResource;
        } 
        else {
            description = "File does not exist OR is not readable! ID: " + fileId;
            logsService.addLogEntry(Severity.WARNING, source, operation, description, logger);
            return null;
        }
    }
}