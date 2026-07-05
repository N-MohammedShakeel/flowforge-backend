package com.ms.flowforge.model.mapper;

import com.ms.flowforge.model.dto.*;
import com.ms.flowforge.model.entity.CanvasState;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ArchitectureMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "version", ignore = true)
    CanvasState toCanvasState(ArchitectureResponse response);

    NodeDto toNodeDto(Object node);   // Flexible mapping from Python JSON

    // You can add more specific mappings as needed
}