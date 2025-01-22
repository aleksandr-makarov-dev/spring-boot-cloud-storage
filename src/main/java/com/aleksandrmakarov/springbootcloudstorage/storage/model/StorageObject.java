package com.aleksandrmakarov.springbootcloudstorage.storage.model;

import java.time.ZonedDateTime;

public record StorageObject(
        String prefix,
        String name,
        Boolean isDir,
        Long size,
        ZonedDateTime lastModified
) {
}
