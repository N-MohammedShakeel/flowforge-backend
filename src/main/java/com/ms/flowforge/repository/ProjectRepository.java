package com.ms.flowforge.repository;

import com.ms.flowforge.model.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, String> {

    List<Project> findByOwnerId(String ownerId);

    List<Project> findByStatus(String status);
}