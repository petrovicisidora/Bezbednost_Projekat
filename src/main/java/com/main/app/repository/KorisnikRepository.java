package com.main.app.repository;

import com.main.app.domain.model.Korisnik;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface KorisnikRepository extends JpaRepository<Korisnik, Long> {
    Optional<Korisnik> findByEmail(String email);
    List<Korisnik> findAll();

    void deleteByEmail(String userEmail);

    void deleteRefreshTokenByEmail(String userEmail);
}
