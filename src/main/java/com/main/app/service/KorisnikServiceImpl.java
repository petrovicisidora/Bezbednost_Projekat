package com.main.app.service;

import com.main.app.domain.dto.KorisnikDto;
import com.main.app.domain.model.Korisnik;
import com.main.app.domain.tokens.LoginRequest;
import com.main.app.domain.tokens.TokenProvider;
import com.main.app.domain.tokens.TokenResponse;
import com.main.app.repository.KorisnikRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class KorisnikServiceImpl implements KorisnikService {

    @Autowired
    private KorisnikRepository korisnikRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenProvider tokenProvider;

    private final String salt;

    @Autowired
    public KorisnikServiceImpl(KorisnikRepository korisnikRepository, TokenProvider tokenProvider, PasswordEncoder passwordEncoder) {
        this.korisnikRepository = korisnikRepository;
        this.tokenProvider = tokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.salt = BCrypt.gensalt();
    }

    @Override
    public Korisnik registerKorisnik(KorisnikDto korisnikDto) {
        Optional<Korisnik> existingUser = korisnikRepository.findByEmail(korisnikDto.getEmail());
        if (existingUser.isPresent()) {
            throw new RuntimeException("Korisnik sa datom email adresom već postoji.");
        }

        // validacija korisničkog unosa

        // kombinovanje lozinke i salt-a i hash-ovanje rezultata
        String combinedPassword = korisnikDto.getPassword() + salt;
        String hashedPassword = passwordEncoder.encode(combinedPassword);

        // Dodajte so u hashovanu lozinku
        String hashedPasswordWithSalt = hashedPassword + salt;

        // kreiranje novog korisnika
        Korisnik korisnik = new Korisnik();
        korisnik.setEmail(korisnikDto.getEmail());
        korisnik.setPassword(hashedPasswordWithSalt);
        korisnik.setFirstName(korisnikDto.getFirstName());
        korisnik.setLastName(korisnikDto.getLastName());
        korisnik.setAddress(korisnikDto.getAddress());
        korisnik.setCity(korisnikDto.getCity());
        korisnik.setState(korisnikDto.getState());
        korisnik.setPhoneNumber(korisnikDto.getPhoneNumber());
        korisnik.setJobTitle(korisnikDto.getJobTitle());

        // čuvanje korisnika u bazi i vraćanje sa generisanim ID-em
        return korisnikRepository.save(korisnik);
    }

    public TokenResponse login(LoginRequest loginRequest) {
        Optional<Korisnik> korisnikOptional = korisnikRepository.findByEmail(loginRequest.getEmail());
        if (korisnikOptional.isEmpty()) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        Korisnik korisnik = korisnikOptional.get();
        String enteredPassword = loginRequest.getPassword();
        String hashedPasswordWithSalt = korisnik.getPassword();

        // Provera podudaranosti lozinke
        if (!matchPassword(enteredPassword, hashedPasswordWithSalt)) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        String accessToken = tokenProvider.generateAccessToken(korisnik);
        String refreshToken = tokenProvider.generateRefreshToken(korisnik);
        return new TokenResponse(accessToken, refreshToken);
    }

    private boolean matchPassword(String enteredPassword, String hashedPasswordWithSalt) {
        // Provera podudaranosti lozinke
        String combinedPassword = enteredPassword + salt;
        String hashedEnteredPassword = passwordEncoder.encode(combinedPassword);
        return hashedEnteredPassword.equals(hashedPasswordWithSalt);
    }



}
