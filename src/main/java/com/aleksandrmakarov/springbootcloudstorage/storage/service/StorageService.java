package com.aleksandrmakarov.springbootcloudstorage.storage.service;

import com.aleksandrmakarov.springbootcloudstorage.storage.model.SearchStorageObject;
import com.aleksandrmakarov.springbootcloudstorage.storage.model.StorageObjectModel;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StorageService {

    List<StorageObjectModel> listObjects(String username, String prefix);

    void saveObjects(String username, String prefix, List<MultipartFile> objects);

    void createObject(String username, String prefix, String name);

    void deleteObject(String username, String prefix, String name);

    List<SearchStorageObject> searchObjects(String username, String prefix, String query);

    String downloadObject(String username, String prefix, String name);
}
