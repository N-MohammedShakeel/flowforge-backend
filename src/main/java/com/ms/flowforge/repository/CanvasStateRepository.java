package com.ms.flowforge.repository;

import com.ms.flowforge.model.entity.CanvasState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CanvasStateRepository extends JpaRepository<CanvasState, String> {

    List<CanvasState> findByProjectIdOrderByVersionDesc(String projectId);

    Optional<CanvasState> findByProjectIdAndVersion(String projectId, Integer version);
}