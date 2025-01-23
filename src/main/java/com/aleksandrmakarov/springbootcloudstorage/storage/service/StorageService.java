package com.aleksandrmakarov.springbootcloudstorage.storage.service;

import com.aleksandrmakarov.springbootcloudstorage.storage.model.StorageObjectModel;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StorageService {

    List<StorageObjectModel> findObjects(String prefix);

    void saveObjects(String prefix, List<MultipartFile> objects);

    void createObject(String prefix, String name);

    void deleteObject(String object);
}
