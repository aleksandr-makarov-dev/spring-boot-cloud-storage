package com.aleksandrmakarov.springbootcloudstorage.service;

import com.aleksandrmakarov.springbootcloudstorage.configuration.MinioProperties;
import com.aleksandrmakarov.springbootcloudstorage.mapper.MinioMapper;
import com.aleksandrmakarov.springbootcloudstorage.model.StorageItem;
import io.minio.*;
import io.minio.messages.Item;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MinioStorageService implements StorageService {

    private final MinioMapper mapper;

    private final MinioProperties properties;
    private final MinioClient client;

    @PostConstruct
    public void init() throws Exception {

        BucketExistsArgs bucketExistsArgs = BucketExistsArgs.builder()
                .bucket(properties.getBucketName())
                .build();

        if (!client.bucketExists(bucketExistsArgs)) {

            MakeBucketArgs makeBucketArgs = MakeBucketArgs.builder()
                    .bucket(properties.getBucketName())
                    .build();

            client.makeBucket(makeBucketArgs);

        }

    }

    @Override
    public List<StorageItem> getItemsByPrefix(String prefix) throws Exception {
        List<StorageItem> itemList = new ArrayList<>();

        ListObjectsArgs args = ListObjectsArgs.builder()
                .bucket(properties.getBucketName())
                .prefix(prefix)
                .build();

        Iterable<Result<Item>> items = client.listObjects(args);

        for (Result<Item> item : items) {
            itemList.add(mapper.mapToStorageItem(item.get()));
        }

        return itemList;
    }
}
