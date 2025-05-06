package org.dbp.hackaton.hackaton1.domain;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
