package org.dbp.hackaton.hackaton1.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CompanyDTO {
    private Long id;
    private String name;
    private String ruc;
    private LocalDate subscriptionDate;
    private boolean active;
    private String adminEmail;
}
