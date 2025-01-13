package com.aleksandrmakarovdev.springbootcloudstorage.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StorageObject {

    private String path;

    private String name;

    private boolean isDirectory;

    private Long size;

    private ZonedDateTime lastModified;
}
