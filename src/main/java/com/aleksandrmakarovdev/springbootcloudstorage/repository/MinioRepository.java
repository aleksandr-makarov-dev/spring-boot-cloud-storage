package com.aleksandrmakarovdev.springbootcloudstorage.repository;

import com.aleksandrmakarovdev.springbootcloudstorage.model.StorageObject;
import com.aleksandrmakarovdev.springbootcloudstorage.util.StorageUtils;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
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

    @Value("${spring.minio.bucket}")
    private String bucketName;

    private final MinioClient minioClient;

    public List<StorageObject> findByPrefix(String prefix) {

        ListObjectsArgs args = ListObjectsArgs.builder()
                .bucket(bucketName)
                .prefix(prefix)
                .build();

        Iterable<Result<Item>> results = minioClient.listObjects(args);

        List<StorageObject> objects = new ArrayList<>();

        try {
            for (Result<Item> result : results) {

                Item item = result.get();

                StorageObject storageObject = StorageObject.builder()
                        .path(item.objectName())
                        .name(StorageUtils.getObjectName(item.objectName()))
                        .size(item.size())
                        .isDirectory(item.isDir())
                        .build();

                if (!item.isDir()) {
                    storageObject.setLastModified(item.lastModified());
                }

                objects.add(storageObject);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }

        return objects;
    }
}
