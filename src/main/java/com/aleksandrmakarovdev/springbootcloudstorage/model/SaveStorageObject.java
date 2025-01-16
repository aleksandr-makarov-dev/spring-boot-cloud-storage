package com.aleksandrmakarovdev.springbootcloudstorage.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.ByteArrayInputStream;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaveStorageObject {

    private String name;

    private String contentType;

    private ByteArrayInputStream stream;

    private Long size;
}
