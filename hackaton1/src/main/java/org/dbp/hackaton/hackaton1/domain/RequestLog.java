package org.dbp.hackaton.hackaton1.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
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
    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    private ModelType modelType;

    @ManyToOne
    private User user;

    @ManyToOne
    private Company company;

    private String fileName;
}
