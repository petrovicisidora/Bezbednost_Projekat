package com.main.app.repository;

import com.main.app.domain.model.Cert;
import com.main.app.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CertRepository extends JpaRepository<Cert, Long> {

    Page<Cert> findAllByDeleted(boolean deleted, Pageable pageable);
    Optional<Cert> findOneById(Long id);

}
