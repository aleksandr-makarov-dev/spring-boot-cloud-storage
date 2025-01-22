package com.aleksandrmakarov.springbootcloudstorage.storage.model;

import java.io.ByteArrayInputStream;

public record SaveStorageObject(
        String object,
        Long size,
        String type,
        ByteArrayInputStream stream
) {
}
