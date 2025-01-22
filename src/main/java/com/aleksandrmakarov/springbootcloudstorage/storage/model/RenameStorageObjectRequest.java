package com.aleksandrmakarov.springbootcloudstorage.storage.model;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RenameStorageObjectRequest {

    private String prefix;

    @NotBlank(message = "Original name cannot be empty.")
    private String originalName;

    @NotBlank(message = "New name cannot be empty.")
    private String newName;
}
