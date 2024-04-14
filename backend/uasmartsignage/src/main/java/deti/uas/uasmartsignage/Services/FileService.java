package deti.uas.uasmartsignage.Services;

import deti.uas.uasmartsignage.Models.CustomFile;
import deti.uas.uasmartsignage.Models.FilesClass;
import deti.uas.uasmartsignage.Repositories.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.MalformedURLException;
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

    @Autowired
    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

   
    /**
     * Retrieves and returns a list of all CustomFile stored at root level.
     * 
     * @return A list of all CustomFile stored at root level.
    */
    public List<CustomFile> getFilesAtRoot() {
        List<CustomFile> files = fileRepository.findAllByParentIsNull();
        logger.debug("Retrieved {} files and folders located at root level.", files.size());
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
    public Optional<CustomFile> getFileByName(String fileName) {
        logger.info("Retrieving file with name: {}", fileName);
        Optional<CustomFile> file = fileRepository.findByName(fileName);

        if (file.isEmpty()) {
            logger.warn("File with name '{}' not found", fileName);
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

        //filesystem path
        StringBuilder pathBuilder = new StringBuilder();
        String rootPath = System.getProperty("user.dir");

        //database path
        String parentDirectoryPath = getParentDirectoryPath(customFile);
        pathBuilder.insert(0, parentDirectoryPath);
        pathBuilder.insert(0, rootPath);

        File directory = new File(pathBuilder.toString() + customFile.getName());

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

        //Creating upload dir
        String rootPath = System.getProperty("user.dir");
        File rootDirectory = new File(rootPath + File.separator + "uploads");
        File uploadDir = new File(File.separator + "uploads");

        if (!rootDirectory.exists()) {
            if (rootDirectory.mkdir()) {
                logger.info("Directory created: {}", rootDirectory.getAbsolutePath());
            } 
            else {
                logger.error("Failed to create directory: {}", rootDirectory.getAbsolutePath());
            }
        }

        //adding upload path
        pathBuilder.insert(0, uploadDir + File.separator);
        return pathBuilder.toString();
    }


    /**
     * Creates a new CustomFile from the provided FilesClass and saves it to the repository.
     * 
     * @param file The FilesClass containing information about the file to create (normally an image or video).
     * @return The created CustomFile, or {@code null} if creation fails.
     */
    public CustomFile createFile(FilesClass file) {
        CustomFile customFile;

        if (file.getFile().isEmpty()) {
            logger.info("Provided file is empty.");
            return null;
        }

        // Get information from uploaded file
        String fileName = StringUtils.cleanPath(file.getFile().getOriginalFilename());
        String fileType = file.getFile().getContentType();
        Long fileSize = file.getFile().getSize();

        // Get parent and transform FilesClass onto a CustomFile
        logger.info("Parent ID: {}", file.getParentId());
        Optional<CustomFile> parent = (file.getParentId() != null) ? getFileOrDirectoryById(file.getParentId()) : Optional.empty();
        if (parent.isEmpty()) {
            logger.info("Parent directory not found (Going to root).");
            customFile = new CustomFile(fileName, fileType, fileSize, null);
        }
        else {
            customFile = new CustomFile(fileName, fileType, fileSize, parent.get());
        }


        // Creating file path for database
        Path path = Paths.get(getParentDirectoryPath(customFile) + fileName);
        customFile.setPath(path.toString());

        // Creating file path for filesystem
        StringBuilder pathBuilder = new StringBuilder();
        String rootPath = System.getProperty("user.dir");
        // Get parent path and add the filename
        String parentDirectoryPath = getParentDirectoryPath(customFile) + fileName;
        pathBuilder.insert(0, parentDirectoryPath);
        pathBuilder.insert(0, rootPath);

        Path fileSysPath = Paths.get(pathBuilder.toString());
        

        try {
            logger.info("Creating file with type: " + customFile.getType() + " and name: " + customFile.getName());
            Files.copy(file.getFile().getInputStream(), fileSysPath);
            fileRepository.save(customFile);
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
        Optional<CustomFile> file = fileRepository.findById(id);
        if (file.isEmpty()) {
            logger.warn("File with ID {} not found", id);
            return false;
        } else {
            // Delete file from disk
            File fileToDelete = new File(file.get().getPath());
            if (fileToDelete.delete()) {
                logger.info("File deleted: {}", fileToDelete.getAbsolutePath());
            } else {
                logger.error("Failed to delete file: {}", fileToDelete.getAbsolutePath());
                return false;
            }
            // Delete file from repository
            fileRepository.delete(file.get());
            logger.info("File with ID {} deleted", id);
            return true;
        }
    }


    /**
     * Downloads the file with the specified name from the filesystem.
     *
     * @param fileName The name of the file to download.
     * @return ResponseEntity with the file resource and headers.
     */
    public ResponseEntity<Resource> downloadFileByName(String fileName) throws IOException {
        Optional<CustomFile> customFile = getFileByName(fileName);
        if (customFile.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        String sFilePath = customFile.get().getPath();

        Path filePath = Paths.get(sFilePath);
        Resource fileResource = new UrlResource(filePath.toUri());

        if (fileResource.exists() && fileResource.isReadable()) {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileResource.getFilename() + "\"");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(fileResource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Downloads the file with the specified name.
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
        String rootPath = System.getProperty("user.dir");

        Path filePath = Paths.get(sFilePath);
        Resource fileResource = new UrlResource(filePath.toUri());

        if (fileResource.exists() && fileResource.isReadable()) {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileResource.getFilename() + "\"");

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
            logger.warn("File with ID {} not found", id);
            return null;
        }

        CustomFile updatedFile = file.get();
        updatedFile.setName(customFile.getName());

        String parentDirectoryPath = getParentDirectoryPath(updatedFile);

        File fileToUpdate = new File(updatedFile.getPath());
        File newFile = new File(parentDirectoryPath + customFile.getName());
        if (fileToUpdate.renameTo(newFile)) {
            updatedFile.setPath(parentDirectoryPath + updatedFile.getName());
            logger.info("File renamed: {}", newFile.getAbsolutePath());
            fileRepository.save(updatedFile);
        }
        else {
            logger.error("Failed to rename file: {}", newFile.getAbsolutePath());
        }

        return updatedFile;
    }


}
