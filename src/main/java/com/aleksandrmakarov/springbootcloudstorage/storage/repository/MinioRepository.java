package com.aleksandrmakarov.springbootcloudstorage.storage.repository;

import com.aleksandrmakarov.springbootcloudstorage.storage.model.SaveStorageObject;
import com.aleksandrmakarov.springbootcloudstorage.storage.model.StorageObject;
import com.aleksandrmakarov.springbootcloudstorage.storage.util.StorageUtils;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.Result;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class MinioRepository {

    /**
     * The name of the MinIO bucket, injected from the application properties.
     * Example property: `spring.minio.bucket=my-bucket`.
     */
    @Value("${spring.minio.bucket}")
    private String bucket;

    /**
     * The MinIO client used to interact with the MinIO server.
     * This is injected as a dependency using the `@RequiredArgsConstructor`.
     */
    private final MinioClient minioClient;

    /**
     * Lists all objects in the specified prefix within the configured bucket.
     *
     * @param prefix The prefix to filter objects within the bucket (e.g., "folder/subfolder/").
     * @return A list of `StorageObject` containing details of the objects in the bucket.
     * Each object contains its prefix, name, directory status, size, and last modification date.
     */
    public List<StorageObject> listObjects(final String prefix) {

        // Build the arguments required for listing objects in MinIO.
        ListObjectsArgs args = ListObjectsArgs.builder()
                .bucket(bucket) // Specify the bucket name.
                .prefix(prefix) // Filter objects based on the provided prefix.
                .startAfter(prefix) // Start listing objects after the specified prefix.
                .build();

        // Fetch objects matching the criteria.
        Iterable<Result<Item>> objects = minioClient.listObjects(args);

        List<StorageObject> objectList = new ArrayList<>();

        // Iterate through the fetched objects.
        for (Result<Item> result : objects) {
            try {
                // Retrieve the item from the result.
                Item item = result.get();

                // Normalize the prefix to ensure consistent formatting.
                String normalizedPrefix = StorageUtils.normalizePrefix(prefix);

                // Map the item details to a `StorageObject` and add it to the list.
                objectList.add(new StorageObject(
                        normalizedPrefix,
                        StorageUtils.getObjectName(normalizedPrefix, item.objectName()),
                        item.isDir(),
                        item.size(),
                        item.lastModified()));

            } catch (Exception e) {
                // Log any exceptions that occur while processing the objects.
                log.error("Failed to get object:", e);
            }
        }

        // Return the list of storage objects.
        return objectList;
    }

    /**
     * Saves a storage object to the Minio storage system.
     *
     * @param saveStorageObject the object to be saved, containing details like object name,
     *                          content type, input stream, and size.
     */
    public void save(SaveStorageObject saveStorageObject) {

        try {
            // Build the PutObjectArgs for the Minio client, specifying the bucket, object name,
            // content type, and the input stream for uploading the file.
            PutObjectArgs args = PutObjectArgs.builder()
                    .bucket(bucket) // Specify the bucket where the object will be stored.
                    .object(saveStorageObject.object()) // Set the object name (path).
                    .contentType(saveStorageObject.type()) // Set the content type (MIME type) of the file.
                    .stream(saveStorageObject.stream(), saveStorageObject.size(), -1) // Set the input stream and the size of the file.
                    .build();

            // Upload the object to Minio.
            minioClient.putObject(args);

        } catch (Exception e) {
            // If an error occurs during the upload, log the exception with an error message.
            log.error("Failed to save object:", e);
        }
    }

}

