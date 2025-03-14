package com.aleksandrmakarov.springbootcloudstorage.mapper;

import com.aleksandrmakarov.springbootcloudstorage.model.StorageItem;
import io.minio.messages.Item;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
public class MinioMapper {
    public StorageItem mapToStorageItem(Item item) {
        StorageItem storageItem = new StorageItem(
                item.objectName(),
                item.objectName(),
                item.isDir(),
                null
        );


        if (!item.isDir()) {
            storageItem.setLastModified(item.lastModified().toLocalDateTime());
        }

        return storageItem;
    }
}
