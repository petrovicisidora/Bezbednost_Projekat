package com.main.app.controller;

import com.main.app.domain.dto.KorisnikDto;
import com.main.app.domain.model.Korisnik;
import com.main.app.service.KorisnikService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/korisnik")
public class KorisnikController {

        @Autowired
        private KorisnikService korisnikService;

        @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
        public ResponseEntity<?> registerKorisnik(@RequestBody KorisnikDto korisnikDto) {
            Korisnik korisnik = korisnikService.registerKorisnik(korisnikDto);
            return ResponseEntity.ok(korisnik);
        }

    }

