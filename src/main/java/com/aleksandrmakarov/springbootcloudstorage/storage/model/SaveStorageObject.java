package com.aleksandrmakarov.springbootcloudstorage.storage.model;

public record SaveStorageObject(
        String object,
        Long size,
        String contentType,
        byte[] bytes
) {
}
