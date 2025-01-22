package com.aleksandrmakarov.springbootcloudstorage.storage.service;

import com.aleksandrmakarov.springbootcloudstorage.storage.model.SaveStorageObject;
import com.aleksandrmakarov.springbootcloudstorage.storage.model.StorageObject;
import com.aleksandrmakarov.springbootcloudstorage.storage.model.StorageObjectModel;
import com.aleksandrmakarov.springbootcloudstorage.storage.repository.MinioRepository;
import com.aleksandrmakarov.springbootcloudstorage.storage.util.StorageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DefaultStorageService implements StorageService {

    private final MinioRepository minioRepository;

    @Override
    public List<StorageObjectModel> findObjects(String prefix) {
        List<StorageObject> objectList = minioRepository.listObjects(prefix);

        return objectList.stream()
                .map(o -> new StorageObjectModel(
                        o.prefix(),
                        o.name(),
                        o.isDir(),
                        StorageUtils.formatSize(o.size()),
                        StorageUtils.formatZonedDateTime(o.lastModified())
                ))
                .toList();
    }

    @Override
    public void saveObjects(String prefix, List<MultipartFile> objects) {
        for (MultipartFile object : objects) {
            try {
                minioRepository.save(
                        new SaveStorageObject(prefix + object.getOriginalFilename(),
                        object.getSize(),
                        object.getContentType(),
                        new ByteArrayInputStream(object.getBytes())
                ));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
