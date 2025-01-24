package com.aleksandrmakarov.springbootcloudstorage.storage.repository;

import com.aleksandrmakarov.springbootcloudstorage.storage.model.SaveStorageObject;
import com.aleksandrmakarov.springbootcloudstorage.storage.model.StorageObject;
import com.aleksandrmakarov.springbootcloudstorage.storage.util.StorageUtils;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
    public List<StorageObject> listObjects(final String prefix, boolean recursive, String startAfter) {

        // Build the arguments required for listing objects in MinIO.
        ListObjectsArgs.Builder argsBuilder = ListObjectsArgs.builder()
                .bucket(bucket) // Specify the bucket name.
                .prefix(prefix) // Filter objects based on the provided prefix.
                .recursive(recursive);

        if (startAfter != null) {
            argsBuilder.startAfter(startAfter); // Start listing objects after the specified prefix
        }

        // Fetch objects matching the criteria.
        Iterable<Result<Item>> objects = minioClient.listObjects(argsBuilder.build());

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
                        StorageUtils.getPrefix(item.objectName()),
                        StorageUtils.getObjectName(item.objectName()),
                        item.isDir(),
                        item.size(),
                        item.lastModified()));

            } catch (Exception e) {
                // Log any exceptions that occur while processing the objects.
                log.error("Failed to get object:", e);
                throw new RuntimeException(e);
            }
        }

        // Return the list of storage objects.
        return objectList;
    }

    /**
     * Saves a storage object to the Minio storage system.
     *
     * @param saveStorageObject the object to be saved, containing details like object name,
     *                          content contentType, input stream, and size.
     */
    public void saveObject(final SaveStorageObject saveStorageObject) {

        try {
            // Build the PutObjectArgs for the Minio client, specifying the bucket, object name,
            // content contentType, and the input stream for uploading the file.
            PutObjectArgs.Builder argsBuilder = PutObjectArgs.builder()
                    .bucket(bucket) // Specify the bucket where the object will be stored.
                    .object(saveStorageObject.object()) // Set the object name (path).
                    .stream(new ByteArrayInputStream(saveStorageObject.bytes()), saveStorageObject.size(), -1); // Set the input stream and the size of the file

            if (saveStorageObject.contentType() != null) {
                argsBuilder.contentType(saveStorageObject.contentType());
            }

            // Upload the object to Minio.
            minioClient.putObject(argsBuilder.build());

        } catch (Exception e) {
            // If an error occurs during the upload, log the exception with an error message.
            log.error("Failed to saveObject object:", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Removes an object from Minio storage system.
     *
     * @param object the name of the object to be deleted
     */

    public void removeObject(final String object) {

        try {
            RemoveObjectArgs args = RemoveObjectArgs.builder()
                    .bucket(bucket)
                    .object(object)
                    .build();

            minioClient.removeObject(args);
        } catch (Exception e) {
            log.error("Failed to removeObject object:", e);
            throw new RuntimeException(e);
        }
    }

    public String getPreSignedUrl(final String object, final int duration, final TimeUnit unit, final Map<String, String> extraQueryParams) {

        try {
            GetPresignedObjectUrlArgs args = GetPresignedObjectUrlArgs.builder()
                    .bucket(bucket)
                    .method(Method.GET)
                    .object(object)
                    .extraQueryParams(extraQueryParams)
                    .expiry(duration, unit)
                    .build();

            return minioClient.getPresignedObjectUrl(args);
        } catch (
                Exception e) {
            log.error("Failed to removeObject object:", e);
            throw new RuntimeException(e);
        }
    }

}

