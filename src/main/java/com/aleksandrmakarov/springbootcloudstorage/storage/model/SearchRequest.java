package com.aleksandrmakarov.springbootcloudstorage.storage.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchRequest {

    @NotBlank(message = "Search query cannot be empty.")
    private String query;
}
