package com.aleksandrmakarovdev.springbootcloudstorage.repository;

import com.aleksandrmakarovdev.springbootcloudstorage.model.SaveStorageObject;
import com.aleksandrmakarovdev.springbootcloudstorage.model.StorageObject;
import com.aleksandrmakarovdev.springbootcloudstorage.util.StorageUtils;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
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
                .startAfter(prefix)
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

    public void save(SaveStorageObject object) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        PutObjectArgs.Builder argsBuilder = PutObjectArgs.builder()
                .bucket(bucketName)
                .object(object.getName())
                .stream(object.getStream(), object.getSize(), -1);


        if (object.getContentType() != null) {
            argsBuilder.contentType(object.getContentType());
        }

        PutObjectArgs args = argsBuilder.build();

        var response = minioClient.putObject(args);
        log.info(response.toString());
    }

    public void remove(String object) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        RemoveObjectArgs args = RemoveObjectArgs.builder()
                .bucket(bucketName)
                .object(object)
                .build();

        minioClient.removeObject(args);
    }
}
