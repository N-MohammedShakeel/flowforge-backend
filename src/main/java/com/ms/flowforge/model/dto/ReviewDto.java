package com.ms.flowforge.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
    private int overallScore;
    private int architectureScore;
    private int scalability;
    private int maintainability;
    private int security;
    private List<String> issues;
    private List<String> suggestions;
}