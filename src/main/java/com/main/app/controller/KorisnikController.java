package com.main.app.controller;

import com.main.app.domain.dto.KorisnikDto;
import com.main.app.domain.model.Korisnik;
import com.main.app.domain.tokens.LoginRequest;
import com.main.app.domain.tokens.TokenResponse;
import com.main.app.service.KorisnikService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/korisnik")
public class KorisnikController {

    private KorisnikService korisnikService;

    @Autowired
    public KorisnikController(KorisnikService korisnikService) {
        this.korisnikService = korisnikService;
    }

    @GetMapping(value = "/getAll")
    public ResponseEntity<List<Korisnik>> getAllKorisnici() {
        List<Korisnik> korisnici = korisnikService.getAllKorisnici();
        return new ResponseEntity<>(korisnici, HttpStatus.OK);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<Korisnik> editKorisnik(@PathVariable("id") Long korisnikId, @RequestBody KorisnikDto korisnikDto) {
        Korisnik editedKorisnik = korisnikService.editKorisnik(korisnikId, korisnikDto);
        return ResponseEntity.ok(editedKorisnik);
    }


    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerKorisnik(@RequestBody KorisnikDto korisnikDto) {
        Korisnik korisnik = korisnikService.registerKorisnik(korisnikDto);
        return ResponseEntity.ok(korisnik);
    }


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        boolean isAuthenticated = korisnikService.login(loginRequest);

        if (isAuthenticated) {
            // Korisnik je uspešno prijavljen
            return ResponseEntity.ok("Uspešna prijava");
        } else {
            // Pogrešni pristupni podaci
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Neuspešna prijava");
        }
    }
}
