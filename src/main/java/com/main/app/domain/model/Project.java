package com.main.app.domain.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "projekat")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String duration;

    @Column
    private String jobDescription;

    //@ManyToOne
    //@JoinColumn(name = "engineer_id")
    //private Korisnik engineer;
}
