package com.ms.flowforge.service;

import com.ms.flowforge.client.PythonAiClient;
import com.ms.flowforge.model.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AiGatewayService {

    private final PythonAiClient pythonAiClient;

    public ArchitectureResponse generateArchitecture(ArchitectureRequest request) {
        return pythonAiClient.generateArchitecture(request);
    }

    public ArchitectureResponse reviewArchitecture(ReviewRequest request) {
        return pythonAiClient.reviewArchitecture(request);
    }

    public ArchitectureResponse enhanceArchitecture(EnhanceRequest request) {
        return pythonAiClient.enhanceArchitecture(request);
    }
}