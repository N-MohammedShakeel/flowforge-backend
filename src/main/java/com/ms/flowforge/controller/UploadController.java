package com.ms.flowforge.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/upload")
public class UploadController {

    private final String AI_DIR = new File("../flowforge-ai").getAbsolutePath();
    private final String SRS_DIR = AI_DIR + "/uploaded_files/srs";
    private final String PROJECT_DIR = AI_DIR + "/uploaded_files/projects";

    @PostMapping("/srs")
    public ResponseEntity<Map<String, String>> uploadSrs(@RequestParam("file") MultipartFile file) {
        return handleUpload(file, SRS_DIR);
    }

    @PostMapping("/project")
    public ResponseEntity<Map<String, String>> uploadProject(@RequestParam("file") MultipartFile file) {
        return handleUpload(file, PROJECT_DIR);
    }

    private ResponseEntity<Map<String, String>> handleUpload(MultipartFile file, String targetDir) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "File is empty"));
        }

        try {
            File dir = new File(targetDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // Keep original extension, but use UUID for uniqueness
            String originalName = file.getOriginalFilename();
            String extension = "";
            if (originalName != null && originalName.contains(".")) {
                extension = originalName.substring(originalName.lastIndexOf("."));
            }
            String fileName = UUID.randomUUID().toString() + extension;
            Path filePath = Paths.get(targetDir, fileName);

            Files.write(filePath, file.getBytes());

            return ResponseEntity.ok(Map.of("filePath", filePath.toAbsolutePath().toString()));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Failed to save file: " + e.getMessage()));
        }
    }
}
