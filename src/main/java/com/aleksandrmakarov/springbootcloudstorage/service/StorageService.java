package com.aleksandrmakarov.springbootcloudstorage.service;

import com.aleksandrmakarov.springbootcloudstorage.model.StorageItem;

import java.util.List;

public interface StorageService {

    List<StorageItem> getItemsByPrefix(String prefix) throws Exception;

}
