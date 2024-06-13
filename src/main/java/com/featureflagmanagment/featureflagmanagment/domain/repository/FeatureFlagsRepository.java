package com.featureflagmanagment.featureflagmanagment.domain.repository;

import com.featureflagmanagment.featureflagmanagment.persistence.entity.FeatureFlags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FeatureFlagsRepository extends JpaRepository<FeatureFlags, Long> {
    List<FeatureFlags> findByFlagName(String flagName);

    @Query(value = "select * from feature_flag where name = ? and id_project = ?", nativeQuery = true)
    FeatureFlags findByNameAndProjectId(String flagName, Long projectId);

    @Query(value = "SELECT * FROM feature_flag WHERE id_project = ?", nativeQuery = true)
    List<FeatureFlags> findByProjectId(Long projectId);
}
