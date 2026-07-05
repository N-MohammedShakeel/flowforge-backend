package com.ms.flowforge.client;

import com.ms.flowforge.model.dto.ArchitectureRequest;
import com.ms.flowforge.model.dto.ArchitectureResponse;
import com.ms.flowforge.model.dto.EnhanceRequest;
import com.ms.flowforge.model.dto.ReviewRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * HTTP client for the Python FastAPI AI service.
 *
 * All requests are sent with the snake_case RestTemplate so field names
 * match the Python service's Pydantic models (e.g., project_id, srs_file_path).
 *
 * The Python service response is deserialized using the same snake_case mapper
 * and then returned as ArchitectureResponse to the Spring Boot layer.
 */
@Component
@Slf4j
public class PythonAiClient {

    private final RestTemplate restTemplate;
    private final String pythonBaseUrl;

    public PythonAiClient(
            @Qualifier("aiRestTemplate") RestTemplate restTemplate,
            @Value("${spring.ai.python-service.url:http://localhost:8000}") String pythonBaseUrl) {
        this.restTemplate = restTemplate;
        this.pythonBaseUrl = pythonBaseUrl;
    }

    /**
     * Builds standard JSON headers for Python service requests.
     */
    private HttpEntity<?> buildEntity(Object body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(body, headers);
    }

    /**
     * POST /api/v1/workflow/generate
     * Generate initial architecture from idea, SRS, or project ZIP.
     */
    public ArchitectureResponse generateArchitecture(ArchitectureRequest request) {
        String url = pythonBaseUrl + "/api/v1/workflow/generate";
        log.debug("Calling Python AI generate: {}", url);
        try {
            // Build the payload that matches Python's WorkflowRequest model
            Map<String, Object> payload = Map.of(
                "project_id", request.getProjectId() != null ? request.getProjectId() : "",
                "source", request.getSource() != null ? request.getSource() : "IDEA",
                "payload", Map.of("description", request.getDescription() != null ? request.getDescription() : ""),
                "srs_file_path", request.getSrsFilePath() != null ? request.getSrsFilePath() : "",
                "project_zip_path", request.getProjectZipPath() != null ? request.getProjectZipPath() : ""
            );
            return restTemplate.postForObject(url, buildEntity(payload), ArchitectureResponse.class);
        } catch (RestClientException e) {
            log.error("Python AI generate failed: {}", e.getMessage());
            throw new RuntimeException("AI generation service is unavailable. Please try again later.", e);
        }
    }

    /**
     * POST /api/v1/workflow/review
     * Review the current architecture.
     */
    public ArchitectureResponse reviewArchitecture(ReviewRequest request) {
        String url = pythonBaseUrl + "/api/v1/workflow/review";
        log.debug("Calling Python AI review with projectId: {}", request.getProjectId());

        if (request.getProjectId() == null) {
            throw new IllegalArgumentException("projectId cannot be null for review");
        }

        try {
            Map<String, Object> payload = Map.of(
                "project_id", request.getProjectId(),
                "nodes", request.getNodes(),
                "edges", request.getEdges(),
                "current_review", request.getCurrentReview() != null ? request.getCurrentReview() : Map.of()
            );

            return restTemplate.exchange(url, HttpMethod.POST, buildEntity(payload), ArchitectureResponse.class)
                    .getBody();
        } catch (RestClientException e) {
            log.error("Python AI review failed for projectId={}: {}", request.getProjectId(), e.getMessage());
            throw new RuntimeException("AI review service failed: " + e.getMessage(), e);
        }
    }

    /**
     * POST /api/v1/workflow/enhance
     * Enhance the current architecture based on review feedback.
     */
    public ArchitectureResponse enhanceArchitecture(EnhanceRequest request) {
        String url = pythonBaseUrl + "/api/v1/workflow/enhance";
        log.debug("Calling Python AI enhance: {}", url);
        try {
            Map<String, Object> payload = Map.of(
                "project_id", request.getProjectId(),
                "nodes", request.getNodes(),
                "edges", request.getEdges(),
                "review", request.getReview() != null ? request.getReview() : Map.of()
            );

            return restTemplate.exchange(
                    url, HttpMethod.POST, buildEntity(payload), ArchitectureResponse.class
            ).getBody();
        } catch (RestClientException e) {
            log.error("Python AI enhance failed: {}", e.getMessage());
            throw new RuntimeException("AI enhancement service is unavailable. Please try again later.", e);
        }
    }
}