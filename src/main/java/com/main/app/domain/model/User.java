package com.main.app.domain.model;

import com.main.app.domain.dto.UserDTO;
import com.main.app.enums.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;


/**
 * The user entity representing all users of the system.
 */
@Entity
@NoArgsConstructor
@Getter
@Setter
public class User extends AbstractEntity {

    @NotNull
    private String password;

    @NotNull
    private String email;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @Enumerated(EnumType.STRING)
    private Role role;


    public User(@NotNull String email, @NotNull String password, @NotNull String firstName, @NotNull String lastName) {
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User(UserDTO user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.firstName = user.getFirstName();
        this.password = user.getPassword();
        this.lastName = user.getLastName();
    }
}