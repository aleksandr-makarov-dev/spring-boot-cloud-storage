package com.aleksandrmakarov.springbootcloudstorage.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class StorageItem {
    private String name;
    private String path;
    private boolean isDir;
    private LocalDateTime lastModified;
}
