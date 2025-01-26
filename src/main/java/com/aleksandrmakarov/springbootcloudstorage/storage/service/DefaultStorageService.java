package com.aleksandrmakarov.springbootcloudstorage.storage.service;

import com.aleksandrmakarov.springbootcloudstorage.storage.model.SaveStorageObject;
import com.aleksandrmakarov.springbootcloudstorage.storage.model.SearchStorageObject;
import com.aleksandrmakarov.springbootcloudstorage.storage.model.StorageObject;
import com.aleksandrmakarov.springbootcloudstorage.storage.model.StorageObjectModel;
import com.aleksandrmakarov.springbootcloudstorage.storage.repository.MinioRepository;
import com.aleksandrmakarov.springbootcloudstorage.storage.util.StorageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Default implementation of the StorageService interface.
 * Provides methods for managing storage objects (list, upload, etc.).
 */
@Service
@RequiredArgsConstructor
public class DefaultStorageService implements StorageService {

    private final MinioRepository minioRepository; // Repository for interacting with the Minio storage system.

    /**
     * Retrieves a list of storage objects (files/folders) based on the provided prefix.
     * The prefix is used to filter the objects to show only those within a specific folder.
     *
     * @param prefix the folder path (prefix) used to filter the objects.
     * @return a list of StorageObjectModel, which includes details of each object.
     */
    @Override
    public List<StorageObjectModel> listObjects(String username, String prefix) {
        // Get the list of objects from Minio repository.
        String path = buildPathForUser(username, prefix);
        List<StorageObject> objectList = minioRepository.listObjects(path, false, path);

        // Transform the list of StorageObject to StorageObjectModel with formatted size and last modified date.
        return objectList.stream()
                .map(o -> new StorageObjectModel(
                        excludeUsernameFromPrefix(username, o.prefix()), // Prefix (folder path) of the object.
                        o.name(),   // Name of the object.
                        o.isDir(),  // Whether the object is a directory.
                        o.isDir() ? "" : StorageUtils.formatSize(o.size()),  // Formatted size of the object.
                        StorageUtils.formatZonedDateTime(o.lastModified())  // Formatted last modified date.
                ))
                .toList(); // Collect the transformed objects into a list.
    }

    /**
     * Saves a list of uploaded objects to the storage system (Minio).
     * The objects are passed as MultipartFile objects, which are processed and saved to the given prefix (folder path).
     *
     * @param prefix  the folder path where the files should be stored.
     * @param objects a list of MultipartFile objects to be uploaded.
     */
    @Override
    public void saveObjects(String username, String prefix, List<MultipartFile> objects) {
        // Loop through each file in the list of uploaded files.
        for (MultipartFile object : objects) {
            try {
                // Create a SaveStorageObject to hold the object data (filename, size, content contentType, and content).
                minioRepository.saveObject(
                        new SaveStorageObject(
                                buildPathForUser(username, prefix, object.getOriginalFilename()), // Full path including prefix and filename.
                                object.getSize(),  // Size of the file.
                                object.getContentType(), // Content contentType (MIME contentType) of the file.
                                object.getBytes() // ByteArrayInputStream for file content.
                        )
                );
            } catch (Exception e) {
                // In case of any error, throw a runtime exception with the caught error.
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void createObject(String username, String prefix, String name) {
        minioRepository.saveObject(new SaveStorageObject(buildPathForUser(username, prefix, name) + "/", 0L, null, new byte[]{}));
    }

    @Override
    public void deleteObject(String username, String prefix, String name) {

        List<String> objectList = null;

        String path = buildPathForUser(username, prefix, name);

        if (path.endsWith("/")) {
            objectList = minioRepository.listObjects(path, true, null)
                    .stream()
                    .map(o -> o.prefix() + o.name())
                    .toList();
        } else {
            objectList = List.of(path);
        }

        for (String obj : objectList) {
            minioRepository.removeObject(obj);
        }
    }

    @Override
    public List<SearchStorageObject> searchObjects(String username, String prefix, String query) {
        List<StorageObject> objectList = minioRepository.listObjects(buildPathForUser(username, prefix), true, null);

        return objectList.stream()
                .filter(o -> !o.name().endsWith("/") && o.name().toLowerCase().contains(query.toLowerCase()))
                .map(o -> new SearchStorageObject(o.name(), excludeUsernameFromPrefix(username, o.prefix())))
                .toList();
    }

    @Override
    public String downloadObject(String username, String prefix, String name) {

        Map<String, String> extraQueryParams = new HashMap<>();

        extraQueryParams.put("response-content-disposition", "attachment; filename=\"" + name + "\"");

        return minioRepository.getPreSignedUrl(
                buildPathForUser(username, prefix, name),
                2,
                TimeUnit.HOURS,
                extraQueryParams
        );
    }

    private String buildPathForUser(String username, String... parts) {
        // Filter out empty strings from parts and join them
        String filteredParts = Arrays.stream(parts)
                .filter(part -> part != null && !part.isEmpty()) // Remove null or empty strings
                .collect(Collectors.joining("/"));
        // Join the username and the filtered parts
        return String.join("/", username, filteredParts);
    }

    private String excludeUsernameFromPrefix(String username, String prefix) {
        return prefix.replace(username + "/", "");
    }
}
