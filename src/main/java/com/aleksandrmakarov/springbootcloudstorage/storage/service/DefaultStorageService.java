package com.aleksandrmakarov.springbootcloudstorage.storage.service;

import com.aleksandrmakarov.springbootcloudstorage.storage.model.SaveStorageObject;
import com.aleksandrmakarov.springbootcloudstorage.storage.model.StorageObject;
import com.aleksandrmakarov.springbootcloudstorage.storage.model.StorageObjectModel;
import com.aleksandrmakarov.springbootcloudstorage.storage.repository.MinioRepository;
import com.aleksandrmakarov.springbootcloudstorage.storage.util.StorageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.List;

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
    public List<StorageObjectModel> findObjects(String prefix) {
        // Get the list of objects from Minio repository.
        List<StorageObject> objectList = minioRepository.listObjects(prefix);

        // Transform the list of StorageObject to StorageObjectModel with formatted size and last modified date.
        return objectList.stream()
                .map(o -> new StorageObjectModel(
                        o.prefix(), // Prefix (folder path) of the object.
                        o.name(),   // Name of the object.
                        o.isDir(),  // Whether the object is a directory.
                        StorageUtils.formatSize(o.size()),  // Formatted size of the object.
                        StorageUtils.formatZonedDateTime(o.lastModified())  // Formatted last modified date.
                ))
                .toList(); // Collect the transformed objects into a list.
    }

    /**
     * Saves a list of uploaded objects to the storage system (Minio).
     * The objects are passed as MultipartFile objects, which are processed and saved to the given prefix (folder path).
     *
     * @param prefix the folder path where the files should be stored.
     * @param objects a list of MultipartFile objects to be uploaded.
     */
    @Override
    public void saveObjects(String prefix, List<MultipartFile> objects) {
        // Loop through each file in the list of uploaded files.
        for (MultipartFile object : objects) {
            try {
                // Create a SaveStorageObject to hold the object data (filename, size, content type, and content).
                minioRepository.save(
                        new SaveStorageObject(
                                prefix + object.getOriginalFilename(), // Full path including prefix and filename.
                                object.getSize(),  // Size of the file.
                                object.getContentType(), // Content type (MIME type) of the file.
                                new ByteArrayInputStream(object.getBytes()) // ByteArrayInputStream for file content.
                        )
                );
            } catch (Exception e) {
                // In case of any error, throw a runtime exception with the caught error.
                throw new RuntimeException(e);
            }
        }
    }
}
