package com.featureflagmanagment.featureflagmanagment.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "feature_flag_environment")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class EnvironmentsFeatureFlags {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_feature_flag", updatable = false)
    private FeatureFlags featureFlag;

    @ManyToOne
    @JoinColumn(name = "id_environment", updatable = false)
    private Environments environments;

    @Column(name = "enabled")
    private boolean enabled = Boolean.FALSE;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
}
