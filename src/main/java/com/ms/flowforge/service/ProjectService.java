package com.ms.flowforge.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms.flowforge.exception.AuthException;
import com.ms.flowforge.exception.ResourceNotFoundException;
import com.ms.flowforge.model.dto.ProjectDto;
import com.ms.flowforge.model.entity.Project;
import com.ms.flowforge.model.entity.CanvasState;
import com.ms.flowforge.repository.ProjectRepository;
import com.ms.flowforge.repository.CanvasStateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Project business logic.
 *
 * Ownership model: every operation that reads or mutates a project
 * verifies that the requesting user's email matches project.ownerId.
 * This prevents one user from accessing another's projects even if they
 * know the project UUID.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final CanvasStateRepository canvasStateRepository;
    private final ObjectMapper objectMapper;

    // ===== Create =====

    @Transactional
    public ProjectDto createProject(ProjectDto dto, String ownerEmail) {
        Project project = new Project();
        project.setName(dto.getName());
        project.setDescription(dto.getDescription());
        project.setOwnerId(ownerEmail);
        project.setStatus("DRAFT");
        if (dto.getTags() != null) {
            project.setTags(dto.getTags());
        }

        Project saved = projectRepository.save(project);
        log.info("Project created: {} by {}", saved.getId(), ownerEmail);
        return mapToDto(saved);
    }

    // ===== Read =====

    @Transactional(readOnly = true)
    public List<ProjectDto> getProjectsByOwner(String ownerEmail) {
        return projectRepository.findByOwnerIdOrderByCreatedAtDesc(ownerEmail)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProjectDto getProjectById(String id, String ownerEmail) {
        Project project = findAndVerifyOwnership(id, ownerEmail);
        return mapToDto(project);
    }

    // ===== Update =====

    @Transactional
    public ProjectDto updateProject(String id, ProjectDto dto, String ownerEmail) {
        Project project = findAndVerifyOwnership(id, ownerEmail);
        if (dto.getName() != null) project.setName(dto.getName());
        if (dto.getDescription() != null) project.setDescription(dto.getDescription());
        if (dto.getStatus() != null) project.setStatus(dto.getStatus());
        if (dto.getTags() != null) project.setTags(dto.getTags());

        return mapToDto(projectRepository.save(project));
    }

    // ===== Delete =====

    @Transactional
    public void deleteProject(String id, String ownerEmail) {
        Project project = findAndVerifyOwnership(id, ownerEmail);
        projectRepository.delete(project);
        log.info("Project deleted: {} by {}", id, ownerEmail);
    }

    // ===== Canvas Save / Load =====

    @Transactional
    public ProjectDto saveCanvas(String id, Map<String, Object> canvasData, String ownerEmail) {
        Project project = findAndVerifyOwnership(id, ownerEmail);
        try {
            // Check if we need to create a new version
            List<CanvasState> states = canvasStateRepository.findByProjectIdOrderByVersionDesc(id);
            int nextVersion = states.isEmpty() ? 1 : states.get(0).getVersion() + 1;
            
            // If the request contains a flag for 'minorUpdate', we could overwrite the latest.
            // But for now, we just save a new version on every explicit save/enhance.
            CanvasState state = new CanvasState();
            state.setProject(project);
            state.setVersion(nextVersion);
            state.setNodes(objectMapper.writeValueAsString(canvasData.getOrDefault("nodes", List.of())));
            state.setEdges(objectMapper.writeValueAsString(canvasData.getOrDefault("edges", List.of())));
            
            if (canvasData.containsKey("review")) {
                state.setReview(objectMapper.writeValueAsString(canvasData.get("review")));
            }
            
            canvasStateRepository.save(state);
            
            // Still update project status
            project.setStatus("ACTIVE");
            projectRepository.save(project);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize canvas data", e);
        }
        log.info("Canvas saved (version) for project: {}", id);
        return mapToDto(project);
    }

    @Transactional(readOnly = true)
    public Map<String, Object> loadCanvas(String id, String ownerEmail) {
        Project project = findAndVerifyOwnership(id, ownerEmail);
        
        List<CanvasState> states = canvasStateRepository.findByProjectIdOrderByVersionDesc(id);
        if (states.isEmpty()) {
            return Map.of("nodes", List.of(), "edges", List.of());
        }
        
        CanvasState latest = states.get(0);
        try {
            Object nodes = objectMapper.readValue(latest.getNodes(), Object.class);
            Object edges = objectMapper.readValue(latest.getEdges(), Object.class);
            Object review = latest.getReview() != null ? objectMapper.readValue(latest.getReview(), Object.class) : null;
            
            return Map.of(
                "nodes", nodes,
                "edges", edges,
                "review", review != null ? review : Map.of()
            );
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize canvas for project {}: {}", id, e.getMessage());
            return Map.of("nodes", List.of(), "edges", List.of());
        }
    }

    // ===== Private helpers =====

    private Project findAndVerifyOwnership(String projectId, String ownerEmail) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", projectId));

        if (!project.getOwnerId().equals(ownerEmail)) {
            // Return 401 not 403 to avoid leaking that the project exists
            throw new AuthException("You do not have access to this project");
        }
        return project;
    }

    private ProjectDto mapToDto(Project project) {
        ProjectDto dto = new ProjectDto();
        dto.setId(project.getId());
        dto.setName(project.getName());
        dto.setDescription(project.getDescription());
        dto.setOwnerId(project.getOwnerId());
        dto.setStatus(project.getStatus());
        dto.setTags(project.getTags());
        dto.setCreatedAt(project.getCreatedAt());
        dto.setUpdatedAt(project.getUpdatedAt());
        return dto;
    }
}