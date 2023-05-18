package com.main.app.domain.model;

import com.main.app.enums.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor

@Table(name = "korisnik")
public class Korisnik implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    @Column
    private String password;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String address;

    @Column
    private String city;

    @Column
    private String state;

    @Column
    private String phoneNumber;

    @Column
    private Roles jobTitle;

    @OneToMany(mappedBy = "worker", fetch = FetchType.LAZY)
    private List<EmployeeInProject> projects;

}
