package com.aleksandrmakarov.springbootcloudstorage.storage.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateStorageObjectRequest {

    private String prefix;

    @NotBlank(message = "Name cannot be empty.")
    private String name;

}
