package com.main.app.service;

import com.main.app.domain.model.Korisnik;
import com.main.app.domain.model.KorisnikSkill;
import com.main.app.domain.model.Skill;
import com.main.app.repository.KorisnikRepository;
import com.main.app.repository.KorisnikSkillRepository;
import com.main.app.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KorisnikSkillServiceImpl implements KorisnikSkillService {
    @Autowired
    KorisnikSkillRepository korisnikSkillRepository;

    @Autowired
    SkillRepository skillRepository;

    @Autowired
    KorisnikRepository korisnikRepository;

    public KorisnikSkillServiceImpl(KorisnikSkillRepository korisnikSkillRepository) {
        this.korisnikSkillRepository = korisnikSkillRepository;
    }

    @Override
    public KorisnikSkill addSkillToKorisnik(Long korisnikId, Long skillId) {
        Optional<Korisnik> korisnikOptional = korisnikRepository.findById(korisnikId);
        if (korisnikOptional.isEmpty()) {
            throw new RuntimeException("Korisnik sa datim ID-em ne postoji.");
        }

        Optional<Skill> skillOptional = skillRepository.findById(skillId);
        if (skillOptional.isEmpty()) {
            throw new RuntimeException("Veština sa datim ID-em ne postoji.");
        }

        Korisnik korisnik = korisnikOptional.get();
        Skill skill = skillOptional.get();

        KorisnikSkill korisnikSkill = new KorisnikSkill();
        korisnikSkill.setKorisnik(korisnik);
        korisnikSkill.setSkill(skill);

        korisnikSkillRepository.save(korisnikSkill);

        return korisnikSkill;
    }

    @Override
    public List<KorisnikSkill> getAllSkillsByKorisnikId(Long korisnikId) {
        return korisnikSkillRepository.findByKorisnikId(korisnikId);
    }

    @Override
    public void deleteSkill(Long skillId) {
        korisnikSkillRepository.deleteById(skillId);
    }


}
