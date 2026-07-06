package com.ms.flowforge.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDto {
    private String id;
    private String name;
    private String description;
    private String ownerId;
    private String status;
    private List<String> tags;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Object canvasState;   // Flexible for nodes + edges
    private Object latestReview;  // Stored project-level review
}