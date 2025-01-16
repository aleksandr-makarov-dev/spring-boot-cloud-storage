package com.aleksandrmakarovdev.springbootcloudstorage.service;

import com.aleksandrmakarovdev.springbootcloudstorage.model.SaveStorageObject;
import com.aleksandrmakarovdev.springbootcloudstorage.model.StorageObject;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StorageService {

    List<StorageObject> findByPrefix(String prefix);

    void saveObjects(List<MultipartFile> files, String prefix);

    void removeObject(String object);

    void createObject(String prefix, String name);
}
