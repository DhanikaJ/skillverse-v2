package com.skillverse.controller;

import com.skillverse.service.FileUploadService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "File Upload", description = "File upload endpoints")
public class FileUploadController {

    private final FileUploadService fileUploadService;

    public FileUploadController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    @PostMapping("/upload")
    public UploadResponse uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String url = fileUploadService.uploadFile(file);
            return new UploadResponse(true, "File uploaded successfully", url);
        } catch (Exception e) {
            return new UploadResponse(false, "Upload failed: " + e.getMessage(), null);
        }
    }

    public static class UploadResponse {
        private boolean success;
        private String message;
        private String url;

        public UploadResponse(boolean success, String message, String url) {
            this.success = success;
            this.message = message;
            this.url = url;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }

        public String getUrl() {
            return url;
        }
    }
}

