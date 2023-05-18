package com.main.app.service;


import com.main.app.domain.dto.KorisnikDto;
import com.main.app.domain.model.Korisnik;
import com.main.app.domain.tokens.LoginRequest;
import com.main.app.domain.tokens.TokenResponse;

import java.util.List;

public interface KorisnikService {

        List<Korisnik> getAllKorisnici();
        Korisnik editKorisnik(Long korisnikId, KorisnikDto korisnikDto);
        Korisnik registerKorisnik(KorisnikDto korisnikDto);
        boolean login(LoginRequest loginRequest);
}
