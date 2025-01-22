package com.aleksandrmakarov.springbootcloudstorage.storage.repository;

import com.aleksandrmakarov.springbootcloudstorage.storage.model.StorageObject;
import com.aleksandrmakarov.springbootcloudstorage.storage.util.StorageUtils;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class MinioRepository {

    @Value("${spring.minio.bucket}")
    private String bucket;

    private final MinioClient minioClient;

    public List<StorageObject> listObjects(final String prefix) {

        ListObjectsArgs args = ListObjectsArgs.builder()
                .bucket(bucket)
                .prefix(prefix)
                .startAfter(prefix)
                .build();

        Iterable<Result<Item>> objects = minioClient.listObjects(args);

        List<StorageObject> objectList = new ArrayList<>();

        for (Result<Item> result : objects) {

            try {

                Item item = result.get();

                String normalizedPrefix = StorageUtils.normalizePrefix(prefix);

                objectList.add(new StorageObject(
                        normalizedPrefix,
                        StorageUtils.getObjectName(normalizedPrefix, item.objectName()),
                        item.isDir(),
                        item.size(),
                        item.lastModified()));

            } catch (Exception e) {
                log.error("failed to get object:", e);
            }
        }

        return objectList;
    }

    public void move(final String objectName, final String newObjectName) {



    }
}
