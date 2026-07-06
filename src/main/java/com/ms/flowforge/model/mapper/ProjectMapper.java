package com.ms.flowforge.model.mapper;

import com.ms.flowforge.model.dto.ProjectDto;
import com.ms.flowforge.model.entity.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * MapStruct mapper for Project ↔ ProjectDto conversion.
 *
 * The canvasState field is explicitly ignored on both sides because it is
 * serialized/deserialized manually in ProjectService using Jackson ObjectMapper
 * (JSONB column requires string serialization that MapStruct cannot infer).
 */
@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface ProjectMapper {

    @Mapping(target = "canvasState", ignore = true)
    @Mapping(target = "latestReview", ignore = true)
    ProjectDto toDto(Project project);

    @Mapping(target = "canvasState", ignore = true)
    @Mapping(target = "latestReview", ignore = true)
    Project toEntity(ProjectDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "canvasState", ignore = true)
    @Mapping(target = "latestReview", ignore = true)
    void updateEntityFromDto(ProjectDto dto, @MappingTarget Project project);
}