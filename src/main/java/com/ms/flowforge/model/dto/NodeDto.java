package com.ms.flowforge.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Represents a component/node in the architecture")
public class NodeDto {
    @Schema(description = "Node ID", example = "node-1")
    private String id;
    
    @Schema(description = "Node label", example = "API Gateway")
    private String label;
    
    @Schema(description = "Node type", example = "service")
    private String type;
    
    @Schema(description = "Technology used", example = "Spring Boot")
    private String technology;
    
    @Schema(description = "Node description", example = "API Gateway for routing requests")
    private String description;
    
    @Schema(description = "Additional metadata")
    private Map<String, Object> metadata;
}