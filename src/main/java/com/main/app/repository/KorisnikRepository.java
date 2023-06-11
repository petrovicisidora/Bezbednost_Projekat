package com.main.app.repository;

import com.main.app.domain.model.Korisnik;
import com.main.app.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface KorisnikRepository extends JpaRepository<Korisnik, Long> {
    Optional<Korisnik> findByEmail(String email);
    List<Korisnik> findAll();

    void deleteByEmail(String userEmail);

    void deleteRefreshTokenByEmail(String userEmail);

    List<Korisnik> findByStatus(Status status);

    List<Korisnik> findByFirstNameContainingAndLastNameContainingAndEmailContaining(String firstName, String lastName, String email);
    List<Korisnik>findByFirstNameStartingWithOrLastNameStartingWithOrEmailStartingWith(String firstName, String lastName, String email);

    List<Korisnik> findByJobTitleAndFirstNameStartingWithOrLastNameStartingWithOrEmailStartingWith(String jobTitle, String firstName, String lastName, String email);
    List<Korisnik> findByJobTitleAndFirstNameStartingWithOrJobTitleAndLastNameStartingWithOrJobTitleAndEmailStartingWith(String jobTitle, String firstName, String jobTitle2, String lastName, String jobTitle3, String email);




    List<Korisnik> findByJobTitle(String jobTitle);

}
