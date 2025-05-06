package org.dbp.hackaton.hackaton1.domain;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne
    private Company company;

    @OneToMany(mappedBy = "user")
    private List<UserLimit> limits;

    @OneToMany(mappedBy = "user")
    private List<RequestLog> requests;
}