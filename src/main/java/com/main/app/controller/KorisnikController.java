package com.main.app.controller;

import ch.qos.logback.classic.Logger;
import com.main.app.UserBlockedException;
import com.main.app.domain.dto.UpdatePasswordDto;
import com.main.app.domain.model.Notification;
import com.main.app.repository.NotificationRepository;
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

import com.main.app.service.RegisterService;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
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
    private RegisterService registerService;

    @Autowired
    private NotificationRepository notificationRepository;

    private static Map<String, Notification> notifications = new HashMap<>();

    private static final Logger log = (Logger) LoggerFactory.getLogger(KorisnikController.class);

    @Autowired
    public KorisnikController(KorisnikService korisnikService,KorisnikRepository korisnikRepository,TokenProvider tokenProvider) {
        this.korisnikService = korisnikService;
        this.korisnikRepository=korisnikRepository;
        this.tokenProvider=tokenProvider;
    }

    private String generateResetCode() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
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
         try {
             Korisnik korisnik = korisnikService.registerKorisnik(korisnikDto);
             return ResponseEntity.ok(korisnik);
         } catch(Exception e){
             log.error("Error registering user!");
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
         }
    }

    @PostMapping(value = "/registerAdmin", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerAdmin(@RequestBody KorisnikDto korisnikDto) {
        try {
            Korisnik korisnik = korisnikService.registerAdmin(korisnikDto);
            log.info("Admin successfully registered!");
            return ResponseEntity.ok(korisnik);
        } catch (Exception e) {
            log.error("Error registering admin");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error registering admin");
        }
    }


    /*@PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest loginRequest) {
        TokenResponse tokenResponse = korisnikService.loginAndGetTokens(loginRequest);

        if (tokenResponse != null) {
            return ResponseEntity.ok(tokenResponse);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

    }*/

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest loginRequest) {
        try {
            TokenResponse tokenResponse = korisnikService.loginAndGetTokens(loginRequest);

            if (tokenResponse != null) {
                log.info("Korisnik se uspješno prijavio: {}", loginRequest.getEmail());
                return ResponseEntity.ok(tokenResponse);
            } else {
                log.error("Korisnik pokusava da se prijavi sa losim kredencijalima: {}", loginRequest.getEmail());

                // Provera i ažuriranje notifikacija
                Notification notification = notificationRepository.findByEmail(loginRequest.getEmail());
                LocalDateTime currentTime = LocalDateTime.now();

                if (notification != null) {
                    LocalDateTime lastNotificationTime = notification.getTime();
                    if (lastNotificationTime.isBefore(currentTime.minusMinutes(5))) {
                        notification.setCount(1);
                        notification.setCritical(false);
                        notification.setMessage("Security Attack! Bad Credentials!");
                    } else {
                        notification.setCount(notification.getCount() + 1);
                        if (notification.getCount() > 2) {
                            notification.setCritical(true);
                            notification.setMessage("Security Attack! Bad Credentials!");
                        }
                    }
                } else {
                    notification = new Notification();
                    notification.setEmail(loginRequest.getEmail());
                    notification.setCount(1);
                    notification.setMessage("Security Attack! Bad Credentials!");
                    notification.setCritical(false);
                }

                notification.setIsRead(false);
                notification.setTime(currentTime);
                notificationRepository.save(notification);

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (UserBlockedException e) {
            System.out.println(e.getMessage());
            log.error("Blokirani korisnik pokusava da se prijavi: {}", loginRequest.getEmail());
            Notification notification = notificationRepository.findByEmail(loginRequest.getEmail());
            LocalDateTime currentTime = LocalDateTime.now();

            if (notification != null) {
                LocalDateTime lastNotificationTime = notification.getTime();
                if (lastNotificationTime.isBefore(currentTime.minusMinutes(5))) {
                    notification.setCount(1);
                    notification.setCritical(false);
                    notification.setMessage("Security Attack! Blocked User!");
                } else {
                    notification.setCount(notification.getCount() + 1);
                    if (notification.getCount() > 2) {
                        notification.setCritical(true);
                        notification.setMessage("Security Attack! Blocked User!");
                    }
                }
            } else {
                notification = new Notification();
                notification.setEmail(loginRequest.getEmail());
                notification.setCount(1);
                notification.setMessage("Security Attack! Blocked User!");
                notification.setCritical(false);
            }

            notification.setIsRead(false);
            notification.setTime(currentTime);
            notificationRepository.save(notification);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }



    @PostMapping("/loginemail")
    public ResponseEntity<TokenResponse> loginEmail(@RequestBody LoginRequest loginRequest) {
            Calendar calendar = Calendar.getInstance();
            Date now = calendar.getTime();
            String email = loginRequest.getEmail();
            // Add 10 minutes
            calendar.add(Calendar.MINUTE, 10);
            Date expdate = calendar.getTime();
            LoginWithEmail emailLog = LoginWithEmail.builder()
                    .token(UUID.randomUUID())
                    .expirationDate(expdate)
                    .isUsed(false)
                    .build();

            Korisnik k = korisnikService.getKorisnikByEmail(email);

            Korisnik kk = korisnikService.findById(k.getId());
            kk.setEmailLogin(emailLog);
            korisnikRepository.save(kk);

            // Generate the HMAC for the token and registerUserInfoId
            String hmac = HmacUtil.generateHmac(emailLog.getToken().toString() + email, "special-special-secret-key");
            String modifiedHmac = hmac.replace("+", "-");

            String encodedToken = URLEncoder.encode(emailLog.getToken().toString(), StandardCharsets.UTF_8);

            String activationLink = "http://localhost:3000/activate/?token=" + modifiedHmac + "&email=" + email;
            korisnikService.posaljiLoginEmail(kk, "Poslali ste zahtev za login putem emaila, kliknite na link: " + activationLink);
            return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/repair")
    public ResponseEntity<TokenResponse> repairAcc(@RequestBody LoginRequest loginRequest) {
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        String email=loginRequest.getEmail();
        // Add 10 minutes
        calendar.add(Calendar.MINUTE, 10);
        Date expdate = calendar.getTime();
        LoginWithEmail emailLog = LoginWithEmail.builder()
                .token(UUID.randomUUID())
                .expirationDate(expdate)
                .isUsed(false)
                .build();

        Korisnik k= korisnikService.getKorisnikByEmail(email);

        Korisnik kk=korisnikService.findById(k.getId());
        kk.setEmailLogin(emailLog);
        korisnikRepository.save(kk);

        // Generate the HMAC for the token and registerUserInfoId
        String hmac = HmacUtil.generateHmac(emailLog.getToken().toString() + email, "special-special-secret-key");
        String modifiedHmac = hmac.replace("+", "-");

        String encodedToken = URLEncoder.encode(emailLog.getToken().toString(), StandardCharsets.UTF_8);

        String activationLink = "http://localhost:3000/repair/?token="+ modifiedHmac + "&email=" + email;
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


                log.info("Successful login with email!");
                return new ResponseEntity<>(new TokenResponse(accessToken, refreshToken), HttpStatus.OK);
            }

        log.error("Error login with email!");
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
        try {
            Korisnik korisnik = korisnikService.getKorisnikById(korisnikId).orElseThrow(() -> new RuntimeException("Korisnik nije pronađen."));
            korisnikService.prihvatiKorisnika(korisnikId);

            String aktivacijskiLink = registerService.generisiAktivacioniLink(korisnikId);
            korisnikService.posaljiAktivacijskiEmail(Optional.of(korisnik), aktivacijskiLink);
            log.info("Successfully accepted user!");
            return ResponseEntity.ok().build();
        }catch (Exception e){
            log.error("Error accepting user registration!");
            return ResponseEntity.badRequest().build();
        }
    }


    @PostMapping("/reject/{korisnikId}")
    public ResponseEntity<?> odbijKorisnika(@PathVariable Long korisnikId, @RequestBody String razlogOdbijanja) {
        try {
            korisnikService.odbijKorisnika(korisnikId, razlogOdbijanja);
            log.info("Successfully rejected user!");
            return ResponseEntity.ok().build();
        }catch (Exception e){
            log.error("Error rejecting user!");
            return ResponseEntity.badRequest().build();
        }
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

    @PutMapping("/edit-password")
    public ResponseEntity editPassword(@RequestBody UpdatePasswordDto updatePasswordDto) {
         try {
             korisnikService.editPassword(updatePasswordDto);
             log.info("Successfully changed password!");
             return new ResponseEntity(HttpStatus.OK);
         } catch (Exception e){
             log.error("Error changing password!");
             return new ResponseEntity(HttpStatus.BAD_REQUEST);
         }
    }


    @GetMapping("/{search}")
    public List<Korisnik> searchUsers(@PathVariable String search) {
        return korisnikService.searchEngineers( search,  search, search);
    }

    @PutMapping("/block/{email}")
    public ResponseEntity block(@PathVariable("email") String email) {
       try {
           korisnikService.block(email);
           log.info("Successfully blocked user!");
           return new ResponseEntity(HttpStatus.OK);
       }catch (Exception e){
           log.error("Error blocking user!");
           return new ResponseEntity(HttpStatus.BAD_REQUEST);
       }
    }

    @PutMapping("/unblock/{email}")
    public ResponseEntity unblock(@PathVariable("email") String email) {
       try {
           korisnikService.unblock(email);
           log.info("Successfully unblocked user!");
           return new ResponseEntity(HttpStatus.OK);
       } catch (Exception e){
           log.error("Error unblocking user!");
           return new ResponseEntity(HttpStatus.BAD_REQUEST);
       }
    }

    @PostMapping("/reset-sifre")
    public ResponseEntity<String> resetujSifru(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String kod = request.get("kod");
        String novaSifra = request.get("novaSifra");

        boolean uspesnoResetovanje = activationService.resetujSifru(email, kod, novaSifra);

        if (uspesnoResetovanje) {
            log.info("Successfully reseted password!");
            return ResponseEntity.ok("Šifra je uspešno resetovana.");
        } else {
            log.error("Error reseting password!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Došlo je do greške prilikom resetovanja šifre.");
        }
    }


    @PostMapping("/send-code")
    public ResponseEntity<String> poslatiKodZaResetovanje(@RequestParam String email) {
        String kod = generateResetCode();

        activationService.posaljiKodZaResetovanje(email, kod);

        return ResponseEntity.ok("Kod za resetovanje šifre je poslat na mejl.");
    }


}
