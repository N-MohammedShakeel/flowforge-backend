package com.ms.flowforge.controller;

import com.ms.flowforge.model.dto.*;
import com.ms.flowforge.service.AiGatewayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AiController {

    private final AiGatewayService aiGatewayService;

    @PostMapping("/generate")
    public ResponseEntity<ArchitectureResponse> generateArchitecture(@RequestBody ArchitectureRequest request) {
        return ResponseEntity.ok(aiGatewayService.generateArchitecture(request));
    }

    @PostMapping("/review")
    public ResponseEntity<ArchitectureResponse> reviewArchitecture(@RequestBody ReviewRequest request) {
        return ResponseEntity.ok(aiGatewayService.reviewArchitecture(request));
    }

    @PostMapping("/enhance")
    public ResponseEntity<ArchitectureResponse> enhanceArchitecture(@RequestBody EnhanceRequest request) {
        return ResponseEntity.ok(aiGatewayService.enhanceArchitecture(request));
    }
}