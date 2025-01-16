package com.aleksandrmakarovdev.springbootcloudstorage.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RemoveObjectRequest {

    @NotBlank(message = "Object cannot be empty")
    private String object;
}
