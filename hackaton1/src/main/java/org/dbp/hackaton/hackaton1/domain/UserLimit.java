package org.dbp.hackaton.hackaton1.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.dbp.hackaton.hackaton1.domain.converter.DurationAttributeConverter;

import java.time.Duration;

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
    @Convert(converter = DurationAttributeConverter.class)
    private Duration timeWindow;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
