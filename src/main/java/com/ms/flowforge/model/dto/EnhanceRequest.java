package com.ms.flowforge.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnhanceRequest {
    private String projectId;
    private List<NodeDto> nodes;
    private List<EdgeDto> edges;
    private Object review;         // Optional review context
}