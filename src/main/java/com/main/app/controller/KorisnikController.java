package com.main.app.controller;

import com.main.app.service.AktivacijaService;
import com.main.app.domain.dto.KorisnikDto;
import com.main.app.domain.model.Korisnik;
import com.main.app.domain.tokens.LoginRequest;
import com.main.app.domain.tokens.TokenProvider;
import com.main.app.domain.tokens.TokenResponse;
import com.main.app.repository.KorisnikRepository;
import com.main.app.service.KorisnikService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/korisnik")
public class KorisnikController {

    private KorisnikService korisnikService;
    private KorisnikRepository korisnikRepository;
    private TokenProvider tokenProvider;
    @Autowired
    private AktivacijaService activationService;

    @Autowired
    public KorisnikController(KorisnikService korisnikService) {
        this.korisnikService = korisnikService;
    }

    @GetMapping(value = "/getAll")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Korisnik>> getAllKorisnici() {
        List<Korisnik> korisnici = korisnikService.getAllKorisnici();
        return new ResponseEntity<>(korisnici, HttpStatus.OK);
    }


    @PutMapping("/edit/{email}")
    public ResponseEntity<Korisnik> editKorisnik(@PathVariable("email") String email, @RequestBody KorisnikDto korisnikDto) {
        Korisnik editedKorisnik = korisnikService.editKorisnik(email, korisnikDto);
        return ResponseEntity.ok(editedKorisnik);
    }


    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerKorisnik(@RequestBody KorisnikDto korisnikDto) {
        Korisnik korisnik = korisnikService.registerKorisnik(korisnikDto);
        return ResponseEntity.ok(korisnik);
    }

    @PostMapping(value = "/registerAdmin", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerAdmin(@RequestBody KorisnikDto korisnikDto) {
        Korisnik korisnik = korisnikService.registerAdmin(korisnikDto);
        return ResponseEntity.ok(korisnik);
    }


    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest loginRequest) {
        TokenResponse tokenResponse = korisnikService.loginAndGetTokens(loginRequest);

        if (tokenResponse != null) {
            return ResponseEntity.ok(tokenResponse);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/getUserNameAndSurname/{userId}")
    public String getUserNameAndSurname(@PathVariable Long userId) {
        return korisnikService.getUserNameAndSurname(userId);
    }

    @GetMapping("/getAllUserNamesAndSurnames")
    public List<String> getAllUserNamesAndSurnames() {
        return korisnikService.getAllUserNamesAndSurnames();
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<Korisnik> getKorisnikById(@PathVariable("id") Long id) {
        Optional<Korisnik> korisnikOptional = korisnikService.getKorisnikById(id);

        if (korisnikOptional.isPresent()) {
            Korisnik korisnik = korisnikOptional.get();
            return ResponseEntity.ok(korisnik);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/getByEmail/{email}")
    public ResponseEntity<Korisnik> getKorisnikByEmail(@PathVariable("email") String email) {
        Optional<Korisnik> korisnikOptional = korisnikService.getKorisnikByEmail(email);

        if (korisnikOptional.isPresent()) {
            Korisnik korisnik = korisnikOptional.get();
            return ResponseEntity.ok(korisnik);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {

        String userEmail = request.getUserPrincipal().getName();
        korisnikRepository.deleteRefreshTokenByEmail(userEmail);
        return ResponseEntity.ok("Successfully logged out.");
    }

    @GetMapping("/getUserIdByEmail/{email}")
    public ResponseEntity<Long> getUserIdByEmail(@PathVariable("email") String email) {
        Long userId = korisnikService.getUserIdByEmail(email);
        if (userId != null) {
            return ResponseEntity.ok(userId);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/getPendingKorisnici")
    public ResponseEntity<List<Korisnik>> getKorisniciNaCekanju() {
        List<Korisnik> korisniciNaCekanju = korisnikService.getKorisniciNaCekanju();
        return ResponseEntity.ok(korisniciNaCekanju);
    }

    @PostMapping("accept/{korisnikId}")
    public ResponseEntity<?> prihvatiKorisnika(@PathVariable Long korisnikId) {
        Korisnik korisnik = korisnikService.getKorisnikById(korisnikId).orElseThrow(() -> new RuntimeException("Korisnik nije pronađen."));
        korisnikService.prihvatiKorisnika(korisnikId);

        String aktivacijskiLink = activationService.generisiAktivacioniLink(korisnikId);
        korisnikService.posaljiAktivacijskiEmail(Optional.of(korisnik), aktivacijskiLink);

        return ResponseEntity.ok().build();
    }


    @PostMapping("/reject/{korisnikId}")
    public ResponseEntity<?> odbijKorisnika(@PathVariable Long korisnikId, @RequestBody String razlogOdbijanja) {
        korisnikService.odbijKorisnika(korisnikId, razlogOdbijanja);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{korisnikId}/aktivacija")
    public void posaljiAktivacijskiEmail(@PathVariable Long korisnikId) {
        Optional<Korisnik> korisnik = korisnikService.getKorisnikById(korisnikId);
        String aktivacijskiLink = activationService.generisiAktivacioniLink(korisnikId);
        korisnikService.posaljiAktivacijskiEmail(korisnik, aktivacijskiLink);
    }



    @GetMapping("/getJobTitleByEmail/{email}")
    public ResponseEntity<String> getJobTitleByEmail(@PathVariable("email") String email) {
        String jobTitle = korisnikService.getJobTitleByEmail(email);
        if (jobTitle != null) {
            return ResponseEntity.ok(jobTitle);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/aktivacija/{korisnikId}")
    public ResponseEntity<String> aktivirajKorisnika(@PathVariable Long korisnikId, @RequestParam("timestamp") String timestamp, @RequestParam("hmac") String hmac) {
        boolean isValid = korisnikService.proveriAktivacijskiLink(korisnikId, timestamp, hmac);

        if (isValid) {
            korisnikService.aktivirajKorisnika(korisnikId);
            return ResponseEntity.ok("Korisnik je uspešno aktiviran.");
        } else {
            return ResponseEntity.badRequest().body("Neispravan aktivacijski link.");
        }
    }

    @GetMapping("/korisnici/{search}")
    public List<Korisnik> searchUsers(@PathVariable String search) {
        return korisnikService.searchEngineers( search,  search, search);
    }




}
