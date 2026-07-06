package com.ms.flowforge.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response containing generated architecture data")
public class ArchitectureResponse {
    @Schema(description = "Operation success status")
    private boolean success;
    
    @Schema(description = "Response message", example = "Architecture generated successfully")
    private String message;
    
    @Schema(description = "Full state from Python AI service")
    private Object state;
    
    @Schema(description = "List of architecture nodes")
    private List<NodeDto> nodes;
    
    @Schema(description = "List of architecture edges")
    private List<EdgeDto> edges;
    
    @Schema(description = "Architecture review data")
    private ReviewDto review;
}