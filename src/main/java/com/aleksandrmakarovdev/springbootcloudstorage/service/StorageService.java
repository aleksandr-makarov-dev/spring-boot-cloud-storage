package com.aleksandrmakarovdev.springbootcloudstorage.service;

import com.aleksandrmakarovdev.springbootcloudstorage.model.StorageObject;

import java.util.List;

public interface StorageService {

    List<StorageObject> findByPrefix(String prefix);
}
