package com.main.app.repository;

import com.main.app.domain.model.Cert;
import com.main.app.domain.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

/**
 * JPA repository for management of the User entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findOneByEmail(String email);
    Page<User> findAllByDeleted(boolean deleted, Pageable pageable);
}