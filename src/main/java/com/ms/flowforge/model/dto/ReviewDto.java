package com.ms.flowforge.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Architecture review scores and feedback")
public class ReviewDto {
    @Schema(description = "Overall architecture score (0-100)", example = "85")
    private int overallScore;
    
    @Schema(description = "Architecture quality score (0-100)", example = "90")
    private int architectureScore;
    
    @Schema(description = "Scalability score (0-100)", example = "80")
    private int scalability;
    
    @Schema(description = "Maintainability score (0-100)", example = "85")
    private int maintainability;
    
    @Schema(description = "Security score (0-100)", example = "75")
    private int security;
    
    @Schema(description = "List of identified issues")
    private List<String> issues;
    
    @Schema(description = "List of improvement suggestions")
    private List<String> suggestions;
}