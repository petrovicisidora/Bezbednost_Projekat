package com.main.app.repository;

import com.main.app.domain.model.KorisnikSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KorisnikSkillRepository extends JpaRepository<KorisnikSkill, Long> {
    List<KorisnikSkill> findByKorisnikId(Long korisnikId);
}
