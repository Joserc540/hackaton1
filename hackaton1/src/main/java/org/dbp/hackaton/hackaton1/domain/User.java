package org.dbp.hackaton.hackaton1.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
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