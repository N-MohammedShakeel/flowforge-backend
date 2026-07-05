package com.ms.flowforge.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArchitectureResponse {
    private boolean success;
    private String message;
    private Object state;               // Full state from Python
    private List<NodeDto> nodes;
    private List<EdgeDto> edges;
    private ReviewDto review;
}