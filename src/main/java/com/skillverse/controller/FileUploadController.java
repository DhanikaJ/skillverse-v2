package com.skillverse.controller;

import com.skillverse.security.AccessPolicy;
import com.skillverse.service.FileUploadService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "File Upload", description = "File upload endpoints")
public class FileUploadController {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    private final FileUploadService fileUploadService;
    private final AccessPolicy accessPolicy;

    public FileUploadController(FileUploadService fileUploadService, AccessPolicy accessPolicy) {
        this.fileUploadService = fileUploadService;
        this.accessPolicy = accessPolicy;
    }

    /**
     * Uploads an image, video or PDF. Restricted to instructors and administrators.
     */
    @PreAuthorize("hasAnyRole('INSTRUCTOR','ADMIN')")
    @PostMapping("/upload")
    public ResponseEntity<UploadResponse> uploadFile(@RequestParam("file") MultipartFile file) {
        accessPolicy.requireNotDemoAccount();
        try {
            String url = fileUploadService.uploadFile(file);
            return ResponseEntity.ok(new UploadResponse(true, "File uploaded successfully", url));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new UploadResponse(false, e.getMessage(), null));
        } catch (Exception e) {
            logger.error("File upload failed", e);
            return ResponseEntity.status(500).body(new UploadResponse(false, "Upload failed", null));
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

