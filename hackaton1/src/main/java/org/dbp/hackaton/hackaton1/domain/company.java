package org.dbp.hackaton.hackaton1.domain;

import jakarta.persistence.*;

@Entity
public class company {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String ruc;
    private LocalDate subscriptionDate;
    private boolean active;

    @OneToOne
    private User admin;

    @OneToMany(mappedBy = "company")
    private List<User> users;

    @OneToMany(mappedBy = "company")
    private List<CompanyRestriction> restrictions;
}
