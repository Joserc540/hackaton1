package org.dbp.hackaton.hackaton1.domain;

import jakarta.persistence.*;

import java.time.Duration;
import java.time.Instant;

@Entity
public class UserLimit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ModelType model;

    private int requestLimit;
    private int tokenLimit;
    private Duration window;

    private Instant windowStart;

    @ManyToOne
    private User user;
}
