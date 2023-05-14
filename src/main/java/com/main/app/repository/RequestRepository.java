package com.main.app.repository;

import com.main.app.domain.model.Korisnik;
import com.main.app.domain.model.RegistrationRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RequestRepository extends JpaRepository<RegistrationRequest, Long> {
    Optional<RegistrationRequest> findByEmail(String email);
}