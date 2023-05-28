package com.main.app.controller;


import com.main.app.domain.model.KorisnikSkill;
import com.main.app.service.KorisnikSkillService;
import com.main.app.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/korisnikskill")
public class KorisnikSkillController {

    private KorisnikSkillService korisnikSkillService;

    @Autowired
    public KorisnikSkillController(KorisnikSkillService korisnikSkillService) {
        this.korisnikSkillService = korisnikSkillService;
    }

    @PostMapping("/korisnik/{korisnikId}/skill/{skillId}")
    public ResponseEntity<KorisnikSkill> addSkillToKorisnik(@PathVariable Long korisnikId, @PathVariable Long skillId) {
        KorisnikSkill korisnikSkill = korisnikSkillService.addSkillToKorisnik(korisnikId, skillId);
        return ResponseEntity.status(HttpStatus.CREATED).body(korisnikSkill);
    }

    @GetMapping("/getAllUserSkills/{id}")
    public ResponseEntity<List<KorisnikSkill>> getAllSkillsByKorisnikId(@PathVariable("id") Long korisnikId) {
        List<KorisnikSkill> skills = korisnikSkillService.getAllSkillsByKorisnikId(korisnikId);
        return new ResponseEntity<>(skills, HttpStatus.OK);
    }

    @DeleteMapping("/deleteSkill/{skillId}")
    public ResponseEntity<?> deleteSkill(@PathVariable Long skillId) {
        korisnikSkillService.deleteSkill(skillId);
        return ResponseEntity.ok().build();
    }
}
