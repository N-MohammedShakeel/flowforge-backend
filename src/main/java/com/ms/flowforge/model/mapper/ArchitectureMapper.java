package com.ms.flowforge.model.mapper;

import com.ms.flowforge.model.dto.ArchitectureResponse;
import com.ms.flowforge.model.entity.CanvasState;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for architecture-related conversions.
 *
 * CanvasState.nodes and CanvasState.edges are JSONB-backed String columns.
 * The actual JSON serialization is done in ProjectService using ObjectMapper.
 * These mappings are kept minimal to avoid MapStruct trying to auto-convert
 * complex types to String.
 */
@Mapper(componentModel = "spring")
public interface ArchitectureMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "nodes", ignore = true)
    @Mapping(target = "edges", ignore = true)
    @Mapping(target = "review", ignore = true)
    CanvasState toCanvasState(ArchitectureResponse response);
}