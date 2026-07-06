package com.ms.flowforge.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request payload for enhancing existing architecture")
public class EnhanceRequest {
    @Schema(description = "Project ID", example = "proj-123")
    private String projectId;
    
    @Schema(description = "List of architecture nodes")
    private List<NodeDto> nodes;
    
    @Schema(description = "List of architecture edges")
    private List<EdgeDto> edges;
    private Object review;         // Optional review context
}