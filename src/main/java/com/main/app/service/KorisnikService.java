package com.main.app.service;

import com.main.app.domain.dto.KorisnikDto;
import com.main.app.domain.model.Korisnik;
import com.main.app.domain.tokens.LoginRequest;
import com.main.app.domain.tokens.TokenResponse;

public interface KorisnikService {

        Korisnik registerKorisnik(KorisnikDto korisnikDto);
        TokenResponse login(LoginRequest loginRequest);
}
