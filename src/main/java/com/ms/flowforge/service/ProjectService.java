package com.ms.flowforge.service;

import com.ms.flowforge.model.dto.ProjectDto;
import com.ms.flowforge.model.entity.Project;
import com.ms.flowforge.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final AiGatewayService aiGatewayService;

    public ProjectDto createProject(ProjectDto dto) {
        Project project = new Project();
        project.setName(dto.getName());
        project.setDescription(dto.getDescription());
        project.setOwnerId(dto.getOwnerId());
        project.setStatus("DRAFT");

        Project saved = projectRepository.save(project);
        return mapToDto(saved);
    }

    public Optional<ProjectDto> getProjectById(String id) {
        return projectRepository.findById(id).map(this::mapToDto);
    }

    public List<ProjectDto> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(this::mapToDto)
                .toList();
    }

    // Helper mapper (can be replaced with MapStruct later)
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