package com.ms.flowforge.model.dto;

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
public class ReviewRequest {

    @NotNull(message = "projectId is required")
    private String projectId;

    @NotNull
    private List<NodeDto> nodes;

    @NotNull
    private List<EdgeDto> edges;

    private Object currentReview;
}