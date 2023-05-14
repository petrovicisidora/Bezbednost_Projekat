package com.main.app.domain.model;

import com.main.app.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "RegistrationRequest")
public class RegistrationRequest {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column
    private String email;
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
    private String jobTitle;
    @Column
    private Status status;


}
