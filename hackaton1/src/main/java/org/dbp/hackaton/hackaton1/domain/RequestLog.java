package org.dbp.hackaton.hackaton1.domain;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
public class RequestLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private String prompt;

    @Lob
    private String response;

    private int tokensUsed;
    private Instant timestamp;

    @Enumerated(EnumType.STRING)
    private ModelType model;

    @ManyToOne
    private User user;

    @ManyToOne
    private Company company;

    private String fileName;
}
