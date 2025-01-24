package com.aleksandrmakarov.springbootcloudstorage.storage.service;

import com.aleksandrmakarov.springbootcloudstorage.storage.model.SearchStorageObject;
import com.aleksandrmakarov.springbootcloudstorage.storage.model.StorageObjectModel;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StorageService {

    List<StorageObjectModel> listObjects(String prefix);

    void saveObjects(String prefix, List<MultipartFile> objects);

    void createObject(String prefix, String name);

    void deleteObject(String object);

    List<SearchStorageObject> searchObjects(String query);

    String downloadObject(String object);
}
