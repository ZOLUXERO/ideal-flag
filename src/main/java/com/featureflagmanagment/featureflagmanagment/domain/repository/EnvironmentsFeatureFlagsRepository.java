package com.featureflagmanagment.featureflagmanagment.domain.repository;

import com.featureflagmanagment.featureflagmanagment.persistence.entity.EnvironmentsFeatureFlags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EnvironmentsFeatureFlagsRepository extends JpaRepository<EnvironmentsFeatureFlags, Long> {

    @Query(value = "select ffg.id, ffg.id_environment, ffg.id_feature_flag, ffg.enabled, ffg.created_at from feature_flag_environment ffg left join feature_flag ff on ff.id = ffg.id_feature_flag where ff.id_project = ?", nativeQuery = true)
    List<EnvironmentsFeatureFlags> findFf(Long idProject);

    @Query(value = "SELECT * FROM feature_flag_environment WHERE id_feature_Flag = ? AND id_environment = ?", nativeQuery = true)
    EnvironmentsFeatureFlags findByFlagIdAndEnvId(Long idFeatureFlag, Long idEnvironment);

    @Query(value = "SELECT * FROM feature_flag_environment WHERE id_environment = ?", nativeQuery = true)
    List<EnvironmentsFeatureFlags> findByEnvId(Long idEnvironment);

    @Query(value = "SELECT * FROM feature_flag_environment WHERE id_feature_flag = ?", nativeQuery = true)
    List<EnvironmentsFeatureFlags> findByFlagId(Long idFeatureFlag);

    @Modifying
    @Query(value = "UPDATE feature_flag_environment SET enable = :enabled WHERE id_feature_flag = :idFeatureFlag AND id_environment = :idEnvironment", nativeQuery = true)
    void updateFlagEnabled(@Param(value = "enabled") boolean enable, @Param(value = "idFeatureFlag") Long idFeatureFlag, @Param(value = "idEnvironment") Long idEnvironment);
}
