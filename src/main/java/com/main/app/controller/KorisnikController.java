package com.main.app.controller;

import com.main.app.domain.dto.KorisnikDto;
import com.main.app.domain.model.Korisnik;
import com.main.app.domain.tokens.LoginRequest;
import com.main.app.domain.tokens.TokenResponse;
import com.main.app.service.CurrentKorisnikService;
import com.main.app.service.KorisnikService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/korisnik")
public class KorisnikController {

        @Autowired
        private KorisnikService korisnikService;

        private CurrentKorisnikService currentKorisnikService;

        @Autowired
        public KorisnikController(
                KorisnikService korisnikService,
                CurrentKorisnikService currentKorisnikService
        ) {
            this.korisnikService = korisnikService;
            this.currentKorisnikService = currentKorisnikService;
        }

        @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<?> registerKorisnik(@RequestBody KorisnikDto korisnikDto) {
            Korisnik korisnik = korisnikService.registerKorisnik(korisnikDto);
            return ResponseEntity.ok(korisnik);
        }

       /* @PostMapping("/login")
        public ResponseEntity<String> login(@RequestParam("email") String email, @RequestParam("password") String password) {
            String tokens = korisnikService.login(email, password);
            return ResponseEntity.ok(tokens);
        }*/

        @GetMapping(path = "/current")
        public ResponseEntity<KorisnikDto> getCurrentUser() {
            return new ResponseEntity<>(
                    currentKorisnikService.getCurrentKorisnikDto(),
                    HttpStatus.OK
            );
    }

        @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest loginRequest) {
            TokenResponse tokenResponse = korisnikService.login(loginRequest);
            return ResponseEntity.ok(tokenResponse);
        }



    }

