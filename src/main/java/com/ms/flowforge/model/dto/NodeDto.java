package com.ms.flowforge.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NodeDto {
    private String id;
    private String label;
    private String type;
    private String technology;
    private String description;
    private Map<String, Object> metadata;
}