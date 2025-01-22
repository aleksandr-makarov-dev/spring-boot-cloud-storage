package com.aleksandrmakarov.springbootcloudstorage.storage.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadFilesRequest {

    private String prefix;

    private List<MultipartFile> files;
}
