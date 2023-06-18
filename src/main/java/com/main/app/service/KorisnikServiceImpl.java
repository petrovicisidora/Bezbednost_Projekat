package com.main.app.service;

import com.main.app.UserBlockedException;
import com.main.app.domain.dto.KorisnikDto;
import com.main.app.domain.dto.UpdatePasswordDto;
import com.main.app.domain.model.Korisnik;
import com.main.app.domain.tokens.LoginRequest;
import com.main.app.domain.tokens.TokenProvider;
import com.main.app.domain.tokens.TokenResponse;
import com.main.app.enums.Status;
import com.main.app.repository.KorisnikRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;


import org.springframework.mail.javamail.MimeMessageHelper;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class KorisnikServiceImpl implements KorisnikService {

    @Autowired
    private KorisnikRepository korisnikRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenProvider tokenProvider;

    private final JavaMailSender mailSender;

    @Autowired
    private final RegisterService registerService;


    @Autowired
    public KorisnikServiceImpl(KorisnikRepository korisnikRepository, TokenProvider tokenProvider, PasswordEncoder passwordEncoder, JavaMailSender mailSender, RegisterService registerService) {

        this.korisnikRepository = korisnikRepository;
        this.tokenProvider = tokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
        this.registerService = registerService;

    }

    @Override
    public List<Korisnik> getAllKorisnici() {
        return korisnikRepository.findAll();
    }

    @Override
    public Korisnik editKorisnik(String email, KorisnikDto korisnikDto) {
        Optional<Korisnik> korisnikOptional = korisnikRepository.findByEmail(email);
        if (korisnikOptional.isEmpty()) {
            throw new RuntimeException("Korisnik sa datim ID-em ne postoji.");
        }

        Korisnik korisnik = korisnikOptional.get();

        // email i sifra se ne menjaju
        korisnik.setFirstName(korisnikDto.getFirstName());
        korisnik.setLastName(korisnikDto.getLastName());
        korisnik.setAddress(korisnikDto.getAddress());
        korisnik.setCity(korisnikDto.getCity());
        korisnik.setState(korisnikDto.getState());
        korisnik.setPhoneNumber(korisnikDto.getPhoneNumber());
        korisnik.setJobTitle(korisnikDto.getJobTitle());

        // Čuvanje izmenjenog korisnika u bazi
        return korisnikRepository.save(korisnik);
    }


    @Override
    public String getUserNameAndSurname(Long userId) {
        Optional<Korisnik> korisnik = korisnikRepository.findById(userId);

        if (korisnik != null) {
            // Dobavljanje imena i prezimena korisnika
            String fullName = korisnik.get().getFirstName() + " " + korisnik.get().getLastName();
            return fullName;
        } else {
            return "";
        }
    }

    @Override
    public List<String> getAllUserNamesAndSurnames() {
        List<Korisnik> korisnici = korisnikRepository.findAll();
        List<String> userNamesAndSurnames = new ArrayList<>();

        for (Korisnik korisnik : korisnici) {
            String fullName = korisnik.getFirstName() + " " + korisnik.getLastName();
            userNamesAndSurnames.add(fullName);
        }

        return userNamesAndSurnames;
    }

    @Override
    public Korisnik registerAdmin(KorisnikDto korisnikDto) {
        Optional<Korisnik> existingUser = korisnikRepository.findByEmail(korisnikDto.getEmail());
        if (existingUser.isPresent()) {
            throw new RuntimeException("Korisnik sa datom email adresom već postoji.");
        }
        String sifra = passwordEncoder.encode(korisnikDto.getPassword());

        // kreiranje novog korisnika
        Korisnik korisnik = new Korisnik();
        korisnik.setEmail(korisnikDto.getEmail());
        korisnik.setPassword(sifra);
        korisnik.setFirstName(korisnikDto.getFirstName());
        korisnik.setLastName(korisnikDto.getLastName());
        korisnik.setAddress(korisnikDto.getAddress());
        korisnik.setCity(korisnikDto.getCity());
        korisnik.setState(korisnikDto.getState());
        korisnik.setPhoneNumber(korisnikDto.getPhoneNumber());
        korisnik.setJobTitle(korisnikDto.getJobTitle());
        korisnik.setStatus(Status.APPROVED);

        // čuvanje korisnika u bazi i vraćanje sa generisanim ID-em
        return korisnikRepository.save(korisnik);
    }

    @Override
    public Korisnik registerKorisnik(KorisnikDto korisnikDto) {
        Optional<Korisnik> existingUser = korisnikRepository.findByEmail(korisnikDto.getEmail());
        if (existingUser.isPresent()) {
            throw new RuntimeException("Korisnik sa datom email adresom već postoji.");
        }
        String sifra = passwordEncoder.encode(korisnikDto.getPassword());

        // kreiranje novog korisnika
        Korisnik korisnik = new Korisnik();
        korisnik.setEmail(korisnikDto.getEmail());
        korisnik.setPassword(sifra);
        korisnik.setFirstName(korisnikDto.getFirstName());
        korisnik.setLastName(korisnikDto.getLastName());
        korisnik.setAddress(korisnikDto.getAddress());
        korisnik.setCity(korisnikDto.getCity());
        korisnik.setState(korisnikDto.getState());
        korisnik.setPhoneNumber(korisnikDto.getPhoneNumber());
        korisnik.setJobTitle(korisnikDto.getJobTitle());
        korisnik.setStatus(Status.PENDING);

        // čuvanje korisnika u bazi i vraćanje sa generisanim ID-em
        return korisnikRepository.save(korisnik);
    }

    @Override
    public Optional<Korisnik> getKorisnikById(Long id) {
        return korisnikRepository.findById(id);
    }


    /*@Override
    public TokenResponse loginAndGetTokens(LoginRequest loginRequest) {
        Optional<Korisnik> korisnikOptional = korisnikRepository.findByEmail(loginRequest.getEmail());

        if (korisnikOptional.isPresent()) {
            Korisnik korisnik = korisnikOptional.get();

            if (BCrypt.checkpw(loginRequest.getPassword(), korisnik.getPassword())) {
                // Kreiranje access tokena
                String accessToken = tokenProvider.generateAccessToken(korisnik);
                // Kreiranje refresh tokena
                String refreshToken = tokenProvider.generateRefreshToken(korisnik);

                return new TokenResponse(accessToken, refreshToken);
            }
        }

        return null;
    }*/

    @Override
    public TokenResponse loginAndGetTokens(LoginRequest loginRequest) {
        Optional<Korisnik> korisnikOptional = korisnikRepository.findByEmail(loginRequest.getEmail());

        if (korisnikOptional.isPresent()) {
            Korisnik korisnik = korisnikOptional.get();

            if (korisnik.getStatus() == Status.APPROVED && BCrypt.checkpw(loginRequest.getPassword(), korisnik.getPassword())) {

                if (korisnik.isBlocked()) {
                    throw new UserBlockedException();
                }

                String accessToken = tokenProvider.generateAccessToken(korisnik);
                String refreshToken = tokenProvider.generateRefreshToken(korisnik);

                return new TokenResponse(accessToken, refreshToken);
            }
            if (korisnik.isBlocked()) {
                throw new UserBlockedException();
            }
        }

        return null;
    }


    @Override
    public Korisnik getKorisnikByEmail(String email) {
        Optional<Korisnik> korisnikOptional = korisnikRepository.findByEmail(email);
        if (korisnikOptional.isPresent()) {
            Korisnik korisnik = korisnikOptional.get();
            return korisnik;
        }
        return null;
    }

    @Override
    public void saveKorisnik(Korisnik korisnik) {
        korisnikRepository.save(korisnik);
    }

    @Override
    public Long getUserIdByEmail(String email) {
        Optional<Korisnik> korisnikOptional = korisnikRepository.findByEmail(email);
        if (korisnikOptional.isPresent()) {
            Korisnik korisnik = korisnikOptional.get();
            return korisnik.getId();
        }
        return null;
    }

    @Override

    public Korisnik findById(Long userId) {
      return korisnikRepository.findById(userId).get();
    }

    public void posaljiLoginEmail(Korisnik korisnik, String aktivacijskiLink) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(korisnik.getEmail());
        message.setSubject("Login via email");
        message.setText(aktivacijskiLink);

        try {
            mailSender.send(message);
            System.out.println("Aktivacijski e-mail poslan na: " + korisnik.getEmail());
        } catch (MailException e) {
            System.out.println("Greška prilikom slanja e-pošte: " + e.getMessage());
        }
    }
    public String getJobTitleByEmail(String email) {
        Optional<Korisnik> korisnikOptional = korisnikRepository.findByEmail(email);
        if (korisnikOptional.isPresent()) {
            Korisnik korisnik = korisnikOptional.get();
            return korisnik.getJobTitle();
        }
        return null;
    }

    @Override
    public List<Korisnik> getKorisniciNaCekanju() {
        return korisnikRepository.findByStatus(Status.PENDING);
    }

    @Override
    public void prihvatiKorisnika(Long korisnikId) {
        Korisnik korisnik = korisnikRepository.findById(korisnikId).orElseThrow(() -> new RuntimeException("Korisnik nije pronađen."));

        korisnik.setStatus(Status.PENDING);
        korisnikRepository.save(korisnik);

        String aktivacijskiLink = registerService.generisiAktivacioniLink(korisnikId);
        posaljiAktivacijskiEmail(Optional.of(korisnik), aktivacijskiLink);
    }

    @Override
    public void posaljiAktivacijskiEmail(Optional<Korisnik> korisnik, String aktivacijskiLink) {
        korisnik.ifPresent(korisnikObj -> {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(korisnikObj.getEmail());
            message.setSubject("Aktivacija korisničkog računa");
            message.setText("Poštovani " + korisnikObj.getFirstName() + ",\n\n"
                    + "Hvala vam na registraciji na našoj aplikaciji.\n"
                    + "Kako biste aktivirali svoj korisnički račun, molimo posetite sledeći link:\n"
                    + aktivacijskiLink + "\n\n"
                    + "Srdačan pozdrav,\n"
                    + "Vaša aplikacija");

            try {
                mailSender.send(message);
                System.out.println("Aktivacijski e-mail poslan na: " + korisnikObj.getEmail());
            } catch (MailException e) {
                System.out.println("Greška prilikom slanja e-pošte: " + e.getMessage());
            }
        });
    }

    @Override
    public void odbijKorisnika(Long korisnikId, String razlogOdbijanja) {
        Korisnik korisnik = korisnikRepository.findById(korisnikId)
                .orElseThrow(() -> new RuntimeException("Korisnik nije pronađen."));

        korisnik.setStatus(Status.REJECTED);
        korisnikRepository.save(korisnik);

        // Slanje e-pošte korisniku s razlogom odbijanja
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(korisnik.getEmail());
            helper.setSubject("Registracija odbijena");
            helper.setText("Poštovani " + korisnik.getFirstName() + ",\n\nVaša registracija je odbijena iz sledećeg razloga:\n" + razlogOdbijanja + "\n\nHvala, \nVaša aplikacija");
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Greška prilikom slanja e-pošte: " + e.getMessage());
        }
    }

    @Override
    public boolean proveriAktivacijskiLink(Long korisnikId, String timestamp, String hmac) {
        String podaci = korisnikId + ":" + timestamp;
        String generisaniHmac = registerService.generisiHmac(podaci);

        return hmac.equals(generisaniHmac);
    }


    @Override
    public void aktivirajKorisnika(Long korisnikId) {
        Optional<Korisnik> korisnikOptional = korisnikRepository.findById(korisnikId);
        if (korisnikOptional.isPresent()) {
            Korisnik korisnik = korisnikOptional.get();
            korisnik.setStatus(Status.APPROVED);
            korisnikRepository.save(korisnik);
        } else {
            throw new RuntimeException("Korisnik nije pronađen.");

        }
    }

    @Override
    public void editPassword(UpdatePasswordDto updatePasswordDto) {
        Optional<Korisnik> oldKorisnikOptional = korisnikRepository.findByEmail(updatePasswordDto.getEmail());
        if (oldKorisnikOptional.isEmpty()) {
            return;
        }

        String updatedPassword = updatePasswordDto.getUpdatedPassword();
        String confirmPassword = updatePasswordDto.getConfirmPassword();

        if (!updatedPassword.equals(confirmPassword)) {
            throw new IllegalArgumentException("Potvrda lozinke se ne podudara.");
        }

        if (updatedPassword.length() < 8 || !updatedPassword.matches(".*\\d.*") || !updatedPassword.matches(".*[^a-zA-Z0-9].*")) {
            throw new IllegalArgumentException("Lozinka mora sadržati najmanje 8 karaktera, barem jedan broj i barem jedan specijalni znak.");
        }

        Korisnik oldKorisnik = oldKorisnikOptional.get();
        oldKorisnik.setPassword(passwordEncoder.encode(updatePasswordDto.getUpdatedPassword()));
        korisnikRepository.save(oldKorisnik);
    }

    @Override
    public List<Korisnik> getAdminKorisnici() {
        return korisnikRepository.findByJobTitle("ADMIN");
    }



    @Override
    public List<Korisnik> searchEngineers(String firstName, String lastName, String email) {
        return  korisnikRepository.findByJobTitleAndFirstNameStartingWithOrJobTitleAndLastNameStartingWithOrJobTitleAndEmailStartingWith("INZENJER", firstName, "INZENJER", lastName, "INZENJER", email);
    }

    @Override
    public void block(String email) {
        Optional<Korisnik> oldKorisnikOptional = korisnikRepository.findByEmail(email);
        if(oldKorisnikOptional.isEmpty()){
            return;
        }
        Korisnik oldKorisnik = oldKorisnikOptional.get();
        oldKorisnik.setBlocked(true);
        korisnikRepository.save(oldKorisnik);
    }

    @Override
    public void unblock(String email){
        Optional<Korisnik> oldKorisnikOptional = korisnikRepository.findByEmail(email);
        if(oldKorisnikOptional.isEmpty()){
            return;
        }
        Korisnik oldKorisnik = oldKorisnikOptional.get();
        oldKorisnik.setBlocked(false);
        korisnikRepository.save(oldKorisnik);
    }




}


