package com.ms.flowforge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms.flowforge.model.dto.ProjectDto;
import com.ms.flowforge.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for ProjectController.
 *
 * Uses a minimal test-only SecurityFilterChain that:
 *  - Disables CSRF (matches production config)
 *  - Permits all requests (auth is verified by @WithMockUser / no-auth test)
 * This avoids loading the full SecurityConfig (which requires DB & JWT beans).
 */
@WebMvcTest(ProjectController.class)
@ContextConfiguration(classes = {ProjectController.class, ProjectControllerTest.TestSecurityConfig.class})
class ProjectControllerTest {

    // ===== Minimal test-only security config =====
    @Configuration
    static class TestSecurityConfig {
        @Bean
        public SecurityFilterChain testSecurityFilterChain(HttpSecurity http) throws Exception {
            http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated());
            return http.build();
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProjectDto mockProjectDto;

    @BeforeEach
    void setUp() {
        mockProjectDto = new ProjectDto();
        mockProjectDto.setId("proj-123");
        mockProjectDto.setName("Test Project");
        mockProjectDto.setDescription("A test project description");
    }

    // 1. GET /api/projects — returns list of projects
    @Test
    @WithMockUser(username = "testuser")
    void getMyProjects_ShouldReturnOk() throws Exception {
        when(projectService.getProjectsByOwner("testuser"))
                .thenReturn(List.of(mockProjectDto));

        mockMvc.perform(get("/api/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("proj-123"))
                .andExpect(jsonPath("$[0].name").value("Test Project"));

        verify(projectService, times(1)).getProjectsByOwner("testuser");
    }

    // 2. GET /api/projects/{id} — returns a single project
    @Test
    @WithMockUser(username = "testuser")
    void getProject_ShouldReturnOk() throws Exception {
        when(projectService.getProjectById("proj-123", "testuser"))
                .thenReturn(mockProjectDto);

        mockMvc.perform(get("/api/projects/proj-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("proj-123"))
                .andExpect(jsonPath("$.name").value("Test Project"));
    }

    // 3. POST /api/projects — creates a new project
    @Test
    @WithMockUser(username = "testuser")
    void createProject_ShouldReturnCreated() throws Exception {
        when(projectService.createProject(any(ProjectDto.class), eq("testuser")))
                .thenReturn(mockProjectDto);

        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockProjectDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("proj-123"));
    }

    // 4. PUT /api/projects/{id} — updates a project
    @Test
    @WithMockUser(username = "testuser")
    void updateProject_ShouldReturnOk() throws Exception {
        when(projectService.updateProject(eq("proj-123"), any(ProjectDto.class), eq("testuser")))
                .thenReturn(mockProjectDto);

        mockMvc.perform(put("/api/projects/proj-123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockProjectDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("proj-123"));
    }

    // 5. DELETE /api/projects/{id} — deletes a project
    @Test
    @WithMockUser(username = "testuser")
    void deleteProject_ShouldReturnOk() throws Exception {
        doNothing().when(projectService).deleteProject("proj-123", "testuser");

        mockMvc.perform(delete("/api/projects/proj-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Project deleted successfully"));

        verify(projectService, times(1)).deleteProject("proj-123", "testuser");
    }

    // 6. POST /api/projects/{id}/canvas — saves canvas state
    @Test
    @WithMockUser(username = "testuser")
    void saveCanvas_ShouldReturnOk() throws Exception {
        Map<String, Object> canvasData = Map.of("nodes", List.of());
        when(projectService.saveCanvas(eq("proj-123"), any(), eq("testuser")))
                .thenReturn(mockProjectDto);

        mockMvc.perform(post("/api/projects/proj-123/canvas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(canvasData)))
                .andExpect(status().isOk());
    }

    // 7. GET /api/projects/{id}/canvas — loads canvas state
    @Test
    @WithMockUser(username = "testuser")
    void loadCanvas_ShouldReturnOk() throws Exception {
        Map<String, Object> mockCanvas = Map.of("nodes", List.of("node1"));
        when(projectService.loadCanvas("proj-123", "testuser"))
                .thenReturn(mockCanvas);

        mockMvc.perform(get("/api/projects/proj-123/canvas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nodes[0]").value("node1"));
    }

}

