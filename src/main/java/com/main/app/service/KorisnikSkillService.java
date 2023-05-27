package com.main.app.service;

import com.main.app.domain.model.KorisnikSkill;

import java.util.List;

public interface KorisnikSkillService {
    KorisnikSkill addSkillToKorisnik(Long korisnikId, Long skillId);

    List<KorisnikSkill> getAllSkillsByKorisnikId(Long korisnikId);

    void deleteSkill(Long skillId);
}
