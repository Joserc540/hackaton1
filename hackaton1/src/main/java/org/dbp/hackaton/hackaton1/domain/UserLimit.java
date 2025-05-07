package org.dbp.hackaton.hackaton1.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class UserLimit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private ModelType modelType;
    private int maxRequests;
    private int maxTokens;
    private String timeWindow;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
