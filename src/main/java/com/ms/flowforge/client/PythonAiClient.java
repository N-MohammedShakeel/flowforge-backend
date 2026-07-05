package com.ms.flowforge.client;

import com.ms.flowforge.model.dto.ArchitectureRequest;
import com.ms.flowforge.model.dto.ArchitectureResponse;
import com.ms.flowforge.model.dto.ReviewRequest;
import com.ms.flowforge.model.dto.EnhanceRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PythonAiClient {

    private final RestTemplate restTemplate;
    private final String pythonBaseUrl;

    public PythonAiClient(RestTemplate restTemplate,
                          @Value("${spring.ai.python-service.url:http://localhost:8000}") String pythonBaseUrl) {
        this.restTemplate = restTemplate;
        this.pythonBaseUrl = pythonBaseUrl;
    }

    /**
     * Generate initial architecture from idea/SRS/Project
     */
    public ArchitectureResponse generateArchitecture(ArchitectureRequest request) {
        String url = pythonBaseUrl + "/api/v1/workflow/generate";
        return restTemplate.postForObject(url, request, ArchitectureResponse.class);
    }

    /**
     * Get review for current architecture
     */
    public ArchitectureResponse reviewArchitecture(ReviewRequest request) {
        String url = pythonBaseUrl + "/api/v1/workflow/review";
        HttpEntity<ReviewRequest> entity = new HttpEntity<>(request);
        return restTemplate.exchange(url, HttpMethod.POST, entity, ArchitectureResponse.class).getBody();
    }

    /**
     * Get enhanced architecture
     */
    public ArchitectureResponse enhanceArchitecture(EnhanceRequest request) {
        String url = pythonBaseUrl + "/api/v1/workflow/enhance";
        HttpEntity<EnhanceRequest> entity = new HttpEntity<>(request);
        return restTemplate.exchange(url, HttpMethod.POST, entity, ArchitectureResponse.class).getBody();
    }
}