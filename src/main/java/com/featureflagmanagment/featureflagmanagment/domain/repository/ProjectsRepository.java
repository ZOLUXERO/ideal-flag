package com.featureflagmanagment.featureflagmanagment.domain.repository;

import com.featureflagmanagment.featureflagmanagment.persistence.entity.Projects;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectsRepository extends JpaRepository<Projects, Long> {
    Projects findByProjectName(String projectName);
}
