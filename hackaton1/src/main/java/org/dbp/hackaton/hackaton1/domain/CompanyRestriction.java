package org.dbp.hackaton.hackaton1.domain;

import jakarta.persistence.*;

@Entity
public class CompanyRestriction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ModelType model;

    private int maxRequests;
    private int maxTokens;

    @ManyToOne
    private Company company;
}
