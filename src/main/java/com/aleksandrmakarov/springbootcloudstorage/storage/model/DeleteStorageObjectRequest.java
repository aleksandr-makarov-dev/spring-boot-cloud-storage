package com.aleksandrmakarov.springbootcloudstorage.storage.model;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteStorageObjectRequest {

    private String prefix;

    @NotBlank(message = "Name cannot be empty.")
    private String name;
}
