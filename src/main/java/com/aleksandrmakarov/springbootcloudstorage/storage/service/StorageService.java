package com.aleksandrmakarov.springbootcloudstorage.storage.service;

import com.aleksandrmakarov.springbootcloudstorage.storage.model.StorageObjectModel;

import java.util.List;

public interface StorageService {

    List<StorageObjectModel> findAll(String prefix);

}
