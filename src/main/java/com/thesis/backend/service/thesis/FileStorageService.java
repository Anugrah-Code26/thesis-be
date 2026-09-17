package com.thesis.backend.service.thesis;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String store(MultipartFile file, String subfolder);
    Resource loadAsResource(String path);
}
