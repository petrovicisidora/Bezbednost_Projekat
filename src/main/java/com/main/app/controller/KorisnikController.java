package com.main.app.controller;

import com.main.app.service.AktivacijaService;
import com.main.app.domain.dto.KorisnikDto;
import com.main.app.domain.model.HmacUtil;
import com.main.app.domain.model.Korisnik;
import com.main.app.domain.model.LoginWithEmail;
import com.main.app.domain.tokens.LoginRequest;
import com.main.app.domain.tokens.TokenProvider;
import com.main.app.domain.tokens.TokenResponse;

import com.main.app.repository.KorisnikRepository;
import com.main.app.service.KorisnikService;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.hibernate.id.GUIDGenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/korisnik")
public class KorisnikController {



    private KorisnikService korisnikService;
    private KorisnikRepository korisnikRepository;
    private TokenProvider tokenProvider;
    @Autowired
    private AktivacijaService activationService;

    @Autowired
    public KorisnikController(KorisnikService korisnikService,KorisnikRepository korisnikRepository,TokenProvider tokenProvider) {
        this.korisnikService = korisnikService;
        this.korisnikRepository=korisnikRepository;
        this.tokenProvider=tokenProvider;
    }

    @GetMapping(value = "/getAll")
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

    @PostMapping("/loginemail")
    public ResponseEntity<TokenResponse> loginEmail(@RequestBody LoginRequest loginRequest) {
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        String email=loginRequest.getEmail();
        // Add 10 minutes
        calendar.add(Calendar.MINUTE, 10);
        Date expdate = calendar.getTime();
        LoginWithEmail emailLog = LoginWithEmail.builder()
                .token(UUID.randomUUID())
                .expirationDate(expdate)
                .isUsed(false) // Set it to false initially
                .build();

        Korisnik k= korisnikService.getKorisnikByEmail(email);

        Korisnik kk=korisnikService.findById(k.getId());
        kk.setEmailLogin(emailLog);
        korisnikRepository.save(kk);

        // Generate the HMAC for the token and registerUserInfoId
        String hmac = HmacUtil.generateHmac(emailLog.getToken().toString() + email, "special-special-secret-key");

        String activationLink = "http://localhost:3000/activate/?token="+ hmac + "&email=" + email;
        korisnikService.posaljiLoginEmail(kk,  "Poslali ste zahtev za login putem emaila, kliknite na link: " + activationLink);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/emailActivate")
    public ResponseEntity emailLoginAct(@RequestParam("token") String token, @RequestParam("email") String email) {
        // Retrieve the RegisterUserInfo object using the provided registerUserInfoId
       Korisnik korisnik = korisnikService.getKorisnikByEmail(email);

        // Check if the RegisterUserInfo exists and the token matches

            LoginWithEmail emailtoken = korisnik.getEmailLogin();

            // Check if the token is expired
            if (emailtoken.getExpirationDate().before(new Date())) {
                return ResponseEntity.badRequest().body("Token has expired.");
            }

            // Check if the token has been used before
            if (emailtoken.isUsed()) {
                return ResponseEntity.badRequest().body("Token has already been used.");
            }

            // Generate the expected HMAC for the token and registerUserInfoId
            String expectedHmac = HmacUtil.generateHmac(korisnik.getEmailLogin().getToken() + email, "special-special-secret-key");

            // Verify if the provided HMAC matches the expected HMAC
            if (expectedHmac.equals(token)) {

                    // Kreiranje access tokena
                    String accessToken = tokenProvider.generateAccessToken(korisnik);
                    // Kreiranje refresh tokena
                    String refreshToken = tokenProvider.generateRefreshToken(korisnik);



                return new ResponseEntity<>(new TokenResponse(accessToken, refreshToken), HttpStatus.OK);
            }


        return ResponseEntity.badRequest().body("Invalid activation link.");
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
       Korisnik korisnik = korisnikService.getKorisnikByEmail(email);

        if(korisnik!=null){
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



}
