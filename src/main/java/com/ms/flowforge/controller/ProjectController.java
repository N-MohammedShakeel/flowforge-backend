package com.ms.flowforge.controller;

import com.ms.flowforge.model.dto.ProjectDto;
import com.ms.flowforge.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Projects", description = "Endpoints for project management and canvas operations")
public class ProjectController {

    private final ProjectService projectService;

    // ===== Project CRUD =====

    @PostMapping
    @Operation(summary = "Create project", description = "Create a new project for the authenticated user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Project created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "401", description = "User not authenticated")
    })
    public ResponseEntity<ProjectDto> createProject(
            @Valid @RequestBody ProjectDto dto,
            @AuthenticationPrincipal UserDetails principal) {
        ProjectDto created = projectService.createProject(dto, principal.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    @Operation(summary = "Get all projects", description = "Retrieve all projects belonging to the authenticated user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Projects retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "User not authenticated")
    })
    public ResponseEntity<List<ProjectDto>> getMyProjects(
            @AuthenticationPrincipal UserDetails principal) {
        return ResponseEntity.ok(projectService.getProjectsByOwner(principal.getUsername()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get project by ID", description = "Retrieve a specific project by ID for the authenticated user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Project retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Project not found"),
        @ApiResponse(responseCode = "401", description = "User not authenticated")
    })
    public ResponseEntity<ProjectDto> getProject(
            @Parameter(description = "Project ID", example = "proj-123")
            @PathVariable String id,
            @AuthenticationPrincipal UserDetails principal) {
        return ResponseEntity.ok(projectService.getProjectById(id, principal.getUsername()));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update project", description = "Update an existing project for the authenticated user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Project updated successfully"),
        @ApiResponse(responseCode = "404", description = "Project not found"),
        @ApiResponse(responseCode = "401", description = "User not authenticated")
    })
    public ResponseEntity<ProjectDto> updateProject(
            @Parameter(description = "Project ID", example = "proj-123")
            @PathVariable String id,
            @Valid @RequestBody ProjectDto dto,
            @AuthenticationPrincipal UserDetails principal) {
        return ResponseEntity.ok(projectService.updateProject(id, dto, principal.getUsername()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete project", description = "Delete a project for the authenticated user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Project deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Project not found"),
        @ApiResponse(responseCode = "401", description = "User not authenticated")
    })
    public ResponseEntity<Map<String, String>> deleteProject(
            @Parameter(description = "Project ID", example = "proj-123")
            @PathVariable String id,
            @AuthenticationPrincipal UserDetails principal) {
        projectService.deleteProject(id, principal.getUsername());
        return ResponseEntity.ok(Map.of("message", "Project deleted successfully"));
    }

    // ===== Canvas Save / Load =====

    @PostMapping("/{id}/canvas")
    @Operation(summary = "Save canvas", description = "Save canvas state for a project")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Canvas saved successfully"),
        @ApiResponse(responseCode = "404", description = "Project not found"),
        @ApiResponse(responseCode = "401", description = "User not authenticated")
    })
    public ResponseEntity<ProjectDto> saveCanvas(
            @Parameter(description = "Project ID", example = "proj-123")
            @PathVariable String id,
            @RequestBody Map<String, Object> canvasData,
            @AuthenticationPrincipal UserDetails principal) {
        ProjectDto updated = projectService.saveCanvas(id, canvasData, principal.getUsername());
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}/canvas")
    @Operation(summary = "Load canvas", description = "Load canvas state for a project")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Canvas loaded successfully"),
        @ApiResponse(responseCode = "404", description = "Project not found"),
        @ApiResponse(responseCode = "401", description = "User not authenticated")
    })
    public ResponseEntity<Map<String, Object>> loadCanvas(
            @Parameter(description = "Project ID", example = "proj-123")
            @PathVariable String id,
            @AuthenticationPrincipal UserDetails principal) {
        Map<String, Object> canvas = projectService.loadCanvas(id, principal.getUsername());
        return ResponseEntity.ok(canvas);
    }

    @PostMapping("/{id}/review")
    @Operation(summary = "Save project review", description = "Save review data for a project")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Review saved successfully"),
        @ApiResponse(responseCode = "404", description = "Project not found"),
        @ApiResponse(responseCode = "401", description = "User not authenticated")
    })
    public ResponseEntity<ProjectDto> saveProjectReview(
            @Parameter(description = "Project ID", example = "proj-123")
            @PathVariable String id,
            @RequestBody Object reviewData,
            @AuthenticationPrincipal UserDetails principal) {
        ProjectDto updated = projectService.saveProjectReview(id, reviewData, principal.getUsername());
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/{id}/canvas/versions")
    @Operation(summary = "Create canvas version", description = "Create a new version snapshot of the canvas for a project")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Canvas version created successfully"),
        @ApiResponse(responseCode = "404", description = "Project not found"),
        @ApiResponse(responseCode = "401", description = "User not authenticated")
    })
    public ResponseEntity<ProjectDto> createCanvasVersion(
            @Parameter(description = "Project ID", example = "proj-123")
            @PathVariable String id,
            @RequestBody Map<String, Object> canvasData,
            @AuthenticationPrincipal UserDetails principal) {
        ProjectDto updated = projectService.createCanvasVersion(id, canvasData, principal.getUsername());
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}/canvas/versions")
    @Operation(summary = "Get canvas versions", description = "Retrieve all available canvas versions for a project")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Versions retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Project not found"),
        @ApiResponse(responseCode = "401", description = "User not authenticated")
    })
    public ResponseEntity<List<Integer>> getCanvasVersions(
            @Parameter(description = "Project ID", example = "proj-123")
            @PathVariable String id,
            @AuthenticationPrincipal UserDetails principal) {
        List<Integer> versions = projectService.getCanvasVersions(id, principal.getUsername());
        return ResponseEntity.ok(versions);
    }

    @GetMapping("/{id}/canvas/versions/{version}")
    @Operation(summary = "Load canvas version", description = "Load a specific version of the canvas for a project")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Canvas version loaded successfully"),
        @ApiResponse(responseCode = "404", description = "Project or version not found"),
        @ApiResponse(responseCode = "401", description = "User not authenticated")
    })
    public ResponseEntity<Map<String, Object>> loadCanvasVersion(
            @Parameter(description = "Project ID", example = "proj-123")
            @PathVariable String id,
            @Parameter(description = "Canvas version number", example = "1")
            @PathVariable Integer version,
            @AuthenticationPrincipal UserDetails principal) {
        Map<String, Object> canvas = projectService.loadCanvasVersion(id, version, principal.getUsername());
        return ResponseEntity.ok(canvas);
    }
}