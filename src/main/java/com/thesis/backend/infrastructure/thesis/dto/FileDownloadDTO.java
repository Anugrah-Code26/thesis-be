package com.thesis.backend.infrastructure.thesis.dto;

import lombok.Data;
import org.springframework.core.io.Resource;

@Data
public class FileDownloadDTO {
    private String filename;
    private String contentType;
    private Resource resource;
}
