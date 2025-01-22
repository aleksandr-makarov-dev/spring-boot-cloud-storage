package com.aleksandrmakarov.springbootcloudstorage.storage.service;

import com.aleksandrmakarov.springbootcloudstorage.storage.model.StorageObject;
import com.aleksandrmakarov.springbootcloudstorage.storage.model.StorageObjectModel;
import com.aleksandrmakarov.springbootcloudstorage.storage.repository.MinioRepository;
import com.aleksandrmakarov.springbootcloudstorage.storage.util.StorageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DefaultStorageService implements StorageService {

    private final MinioRepository minioRepository;

    @Override
    public List<StorageObjectModel> findAll(String prefix) {
        List<StorageObject> objectList = minioRepository.listObjects(prefix);

        return objectList.stream()
                .map(o -> new StorageObjectModel(
                        o.prefix(),
                        o.name(),
                        o.isDir(),
                        StorageUtils.formatSize(o.size()),
                        o.lastModified()
                ))
                .toList();
    }
}
