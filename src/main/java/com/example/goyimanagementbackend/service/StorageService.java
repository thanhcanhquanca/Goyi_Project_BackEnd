package com.example.goyimanagementbackend.service;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    void init();

    String store(MultipartFile file);

    String storeWithUUID(MultipartFile file, String subFolder);

    void deleteFile(String fileName);

    boolean exists(String path);
}