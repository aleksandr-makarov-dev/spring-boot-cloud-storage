package com.aleksandrmakarovdev.springbootcloudstorage.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateObjectRequest {

    @NotBlank(message = "Name cannot be empty.")
    private String name;
}
