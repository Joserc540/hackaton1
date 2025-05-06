package org.dbp.hackaton.hackaton1.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCompanyRequest {
    private String name;
    private String ruc;
    private String adminName;
    private String adminEmail;
    private String adminPassword;
}
