package com.aleksandrmakarovdev.springbootcloudstorage.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadObjectRequest {

    // TODO: Validate array size
    @NotNull(message = "File cannot be empty.")
    @Size(min = 1, max = 5, message = "Number of files should be between {min} and {max}.")
    private List<MultipartFile> files = new ArrayList<>();
}
