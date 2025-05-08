package org.dbp.hackaton.hackaton1.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.dbp.hackaton.hackaton1.domain.converter.DurationAttributeConverter;

import java.time.Duration;

@Getter
@Setter
@Entity
public class CompanyRestriction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ModelType model;

    private int maxRequests;
    private int maxTokens;

    @Convert(converter = DurationAttributeConverter.class)
    private Duration timeWindow;

    @ManyToOne
    private Company company;
}
