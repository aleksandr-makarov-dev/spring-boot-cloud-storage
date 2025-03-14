package com.aleksandrmakarov.springbootcloudstorage.controller;

import com.aleksandrmakarov.springbootcloudstorage.model.StorageItem;
import com.aleksandrmakarov.springbootcloudstorage.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/resources")
@RequiredArgsConstructor
public class StorageController {

    private final StorageService storageService;

    @GetMapping()
    public ResponseEntity<?> getItemsByPrefix(@RequestParam(required = false) String path) throws Exception {

        List<StorageItem> items = storageService.getItemsByPrefix(path);

        return ResponseEntity.ok().body(items);
    }


}
