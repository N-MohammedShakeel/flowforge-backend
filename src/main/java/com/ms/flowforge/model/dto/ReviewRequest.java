package com.ms.flowforge.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequest {
    private String projectId;
    private List<NodeDto> nodes;
    private List<EdgeDto> edges;
    private Object currentReview;   // Optional
}