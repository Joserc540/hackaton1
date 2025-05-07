package org.dbp.hackaton.hackaton1.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class ModelRestriction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String modelType;

    private int maxRequests;
    private int maxTokens;

    private String timeWindow;

    @ManyToOne
    private Company company;
}
