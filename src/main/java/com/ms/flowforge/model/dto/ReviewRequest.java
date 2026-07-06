package com.ms.flowforge.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request payload for reviewing architecture")
public class ReviewRequest {

    @NotNull(message = "projectId is required")
    @Schema(description = "Project ID", example = "proj-123", required = true)
    private String projectId;

    @NotNull
    @Schema(description = "List of architecture nodes", required = true)
    private List<NodeDto> nodes;

    @NotNull
    @Schema(description = "List of architecture edges", required = true)
    private List<EdgeDto> edges;

    @Schema(description = "Current review context (optional)")
    private Object currentReview;
}