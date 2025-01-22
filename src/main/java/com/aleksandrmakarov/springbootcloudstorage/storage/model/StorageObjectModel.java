package com.aleksandrmakarov.springbootcloudstorage.storage.model;

import java.time.ZonedDateTime;

public record StorageObjectModel(
        String prefix,
        String name,
        Boolean isDir,
        String size,
        ZonedDateTime lastModified
){
}
