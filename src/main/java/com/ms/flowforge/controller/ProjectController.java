package com.ms.flowforge.controller;

import com.ms.flowforge.model.dto.ProjectDto;
import com.ms.flowforge.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Project REST controller.
 * All endpoints are scoped to the authenticated user — users only see their own projects.
 */
@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    // ===== Project CRUD =====

    @PostMapping
    public ResponseEntity<ProjectDto> createProject(
            @Valid @RequestBody ProjectDto dto,
            @AuthenticationPrincipal UserDetails principal) {
        ProjectDto created = projectService.createProject(dto, principal.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<ProjectDto>> getMyProjects(
            @AuthenticationPrincipal UserDetails principal) {
        return ResponseEntity.ok(projectService.getProjectsByOwner(principal.getUsername()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDto> getProject(
            @PathVariable String id,
            @AuthenticationPrincipal UserDetails principal) {
        return ResponseEntity.ok(projectService.getProjectById(id, principal.getUsername()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectDto> updateProject(
            @PathVariable String id,
            @Valid @RequestBody ProjectDto dto,
            @AuthenticationPrincipal UserDetails principal) {
        return ResponseEntity.ok(projectService.updateProject(id, dto, principal.getUsername()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteProject(
            @PathVariable String id,
            @AuthenticationPrincipal UserDetails principal) {
        projectService.deleteProject(id, principal.getUsername());
        return ResponseEntity.ok(Map.of("message", "Project deleted successfully"));
    }

    // ===== Canvas Save / Load =====

    @PostMapping("/{id}/canvas")
    public ResponseEntity<ProjectDto> saveCanvas(
            @PathVariable String id,
            @RequestBody Map<String, Object> canvasData,
            @AuthenticationPrincipal UserDetails principal) {
        ProjectDto updated = projectService.saveCanvas(id, canvasData, principal.getUsername());
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}/canvas")
    public ResponseEntity<Map<String, Object>> loadCanvas(
            @PathVariable String id,
            @AuthenticationPrincipal UserDetails principal) {
        Map<String, Object> canvas = projectService.loadCanvas(id, principal.getUsername());
        return ResponseEntity.ok(canvas);
    }
}