package com.aleksandrmakarov.springbootcloudstorage.storage.model;

public record StorageObjectModel(
        String prefix,
        String name,
        Boolean isDir,
        String size,
        String lastModified
) {
}
