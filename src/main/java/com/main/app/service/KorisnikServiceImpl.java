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

    @Autowired
    public KorisnikServiceImpl(KorisnikRepository korisnikRepository, TokenProvider tokenProvider, PasswordEncoder passwordEncoder) {
        this.korisnikRepository = korisnikRepository;
        this.tokenProvider = tokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<Korisnik> getAllKorisnici() {
        return korisnikRepository.findAll();
    }

    @Override
    public Korisnik editKorisnik(Long korisnikId, KorisnikDto korisnikDto) {
        Optional<Korisnik> korisnikOptional = korisnikRepository.findById(korisnikId);
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

        // čuvanje korisnika u bazi i vraćanje sa generisanim ID-em
        return korisnikRepository.save(korisnik);
    }

    @Override
    public Optional<Korisnik> getKorisnikById(Long id) {
        return korisnikRepository.findById(id);
    }


    @Override
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
    }

    @Override
    public Optional<Korisnik> getKorisnikByEmail(String email) {
        return korisnikRepository.findByEmail(email);
    }

}

