package com.main.app.service;

import com.main.app.config.SecurityUtils;
import com.main.app.domain.dto.KorisnikDto;
import com.main.app.domain.model.Korisnik;
import com.main.app.repository.KorisnikRepository;
import com.main.app.util.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CurrentKorisnikServiceImpl implements CurrentKorisnikService{

    KorisnikRepository korisnikRepository;

    @Autowired
    public CurrentKorisnikServiceImpl(
            KorisnikRepository korisnikRepository
    ) {
        this.korisnikRepository = korisnikRepository;
    }

    public Optional<Korisnik> getCurrentKorisnik() {
        Optional<String> username = SecurityUtils.getCurrentUserLogin();

        return this.korisnikRepository.findByEmail(username.get());
    }

    public KorisnikDto getCurrentKorisnikDto() {
        Optional<Korisnik> korisnik = getCurrentKorisnik();

        return ObjectMapperUtils.map(korisnik.get(), KorisnikDto.class);
    }


}
