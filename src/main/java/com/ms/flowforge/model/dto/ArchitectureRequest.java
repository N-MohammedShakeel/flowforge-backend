package com.ms.flowforge.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArchitectureRequest {
    private String projectId;
    private String source;           // IDEA, SRS, PROJECT
    private String description;
    private String srsFilePath;      // For future
    private String projectZipPath;   // For future
}