package com.featureflagmanagment.featureflagmanagment.domain.repository;

import com.featureflagmanagment.featureflagmanagment.persistence.entity.Environments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EnvironmentsRepository extends JpaRepository<Environments, Long> {
    List<Environments> findByName(String name);

    @Query(value = "SELECT * FROM environment WHERE id_project = ?", nativeQuery = true)
    List<Environments> findByProjectId(Long projectId);

    @Query(value = "select * from environment where name = ? and id_project = ?", nativeQuery = true)
    Environments findByNameAndProjectId(String flagName, Long projectId);
}
