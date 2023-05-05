package com.main.app.service;

import com.main.app.domain.dto.KorisnikDto;
import com.main.app.domain.model.Korisnik;
import com.main.app.repository.KorisnikRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class KorisnikServiceImpl implements KorisnikService {

    @Autowired
    private KorisnikRepository korisnikRepository;

    @Override
    public Korisnik registerKorisnik(KorisnikDto korisnikDto) {
        Optional<Korisnik> existingUser = korisnikRepository.findByEmail(korisnikDto.getEmail());
        if (existingUser.isPresent()) {
            throw new RuntimeException("Korisnik sa datom email adresom već postoji.");
        }

        // validacija korisničkog unosa

        // generisanje salt-a
        String salt = BCrypt.gensalt();

        // kombinovanje lozinke i salt-a i hash-ovanje rezultata
        String combinedPassword = korisnikDto.getPassword() + salt;
        String hashedPassword = new BCryptPasswordEncoder().encode(combinedPassword);

        // kreiranje novog korisnika
        Korisnik korisnik = new Korisnik();
        korisnik.setEmail(korisnikDto.getEmail());
        korisnik.setPassword(hashedPassword);
        //setSalt? verovatno ne treba
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

}

