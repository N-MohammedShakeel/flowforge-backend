package com.ms.flowforge.model.mapper;

import com.ms.flowforge.model.dto.ProjectDto;
import com.ms.flowforge.model.entity.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ProjectMapper {

    ProjectDto toDto(Project project);

    Project toEntity(ProjectDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(ProjectDto dto, @MappingTarget Project project);
}