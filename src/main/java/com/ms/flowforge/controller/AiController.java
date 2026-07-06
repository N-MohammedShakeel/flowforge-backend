package com.ms.flowforge.controller;

import com.ms.flowforge.model.dto.*;
import com.ms.flowforge.service.AiGatewayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
@Tag(name = "AI Gateway", description = "Endpoints for AI-powered architecture generation, review, and enhancement")
public class AiController {

    private final AiGatewayService aiGatewayService;

    @PostMapping("/generate")
    @Operation(summary = "Generate architecture", description = "Generate a new architecture based on the provided description and source type")
    public ResponseEntity<ArchitectureResponse> generateArchitecture(@RequestBody ArchitectureRequest request) {
        return ResponseEntity.ok(aiGatewayService.generateArchitecture(request));
    }

    @PostMapping("/review")
    @Operation(summary = "Review architecture", description = "Review an existing architecture and provide feedback with scores and suggestions")
    public ResponseEntity<ArchitectureResponse> reviewArchitecture(@RequestBody ReviewRequest request) {
        return ResponseEntity.ok(aiGatewayService.reviewArchitecture(request));
    }

    @PostMapping("/enhance")
    @Operation(summary = "Enhance architecture", description = "Enhance an existing architecture with improvements and optimizations")
    public ResponseEntity<ArchitectureResponse> enhanceArchitecture(@RequestBody EnhanceRequest request) {
        return ResponseEntity.ok(aiGatewayService.enhanceArchitecture(request));
    }
}