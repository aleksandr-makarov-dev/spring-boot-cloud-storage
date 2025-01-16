package com.aleksandrmakarovdev.springbootcloudstorage.service;

import com.aleksandrmakarovdev.springbootcloudstorage.model.SaveStorageObject;
import com.aleksandrmakarovdev.springbootcloudstorage.model.StorageObject;
import com.aleksandrmakarovdev.springbootcloudstorage.repository.MinioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MinioStorageService implements StorageService {

    private final MinioRepository minioRepository;

    @Override
    public List<StorageObject> findByPrefix(String prefix) {
        return minioRepository.findByPrefix(prefix);
    }

    @Override
    public void saveObjects(List<MultipartFile> files, String prefix) {

        for (MultipartFile file : files) {
            try {

                SaveStorageObject object = SaveStorageObject.builder()
                        .name(prefix + file.getOriginalFilename())
                        .contentType(file.getContentType())
                        .size(file.getSize())
                        .stream(new ByteArrayInputStream(file.getBytes()))
                        .build();

                minioRepository.save(object);
            } catch (Exception e) {
                log.error("Exception while saving object", e);
            }
        }
    }

    @Override
    public void removeObject(String object) {
        try {
            minioRepository.remove(object);
        } catch (Exception e) {
            log.error("Exception while removing object", e);
        }
    }

    @Override
    public void createObject(String prefix, String name) {
        SaveStorageObject object = SaveStorageObject.builder()
                .name(prefix + name + '/')
                .size(0L)
                .stream(new ByteArrayInputStream(new byte[]{}))
                .build();

        try {
            minioRepository.save(object);
        } catch (Exception e) {
            log.error("Exception while creating object", e);
        }
    }
}
