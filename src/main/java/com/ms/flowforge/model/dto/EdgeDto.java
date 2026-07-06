package com.ms.flowforge.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Represents a connection/edge between architecture nodes")
public class EdgeDto {
    @Schema(description = "Source node ID", example = "node-1")
    private String source;
    
    @Schema(description = "Target node ID", example = "node-2")
    private String target;
    
    @Schema(description = "Edge type", example = "dependency")
    private String type;
    
    @Schema(description = "Edge label", example = "calls")
    private String label;
}