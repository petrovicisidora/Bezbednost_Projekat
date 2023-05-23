package com.main.app.service;


import com.main.app.domain.dto.KorisnikDto;
import com.main.app.domain.model.Korisnik;
import com.main.app.domain.tokens.LoginRequest;
import com.main.app.domain.tokens.TokenResponse;

import java.util.List;
import java.util.Optional;

public interface KorisnikService {

        List<Korisnik> getAllKorisnici();
        Korisnik editKorisnik(String email, KorisnikDto korisnikDto);
        Korisnik registerKorisnik(KorisnikDto korisnikDto);
        TokenResponse loginAndGetTokens(LoginRequest loginRequest);
        Optional<Korisnik> getKorisnikById(Long id);
        String getUserNameAndSurname(Long userId);
        List<String> getAllUserNamesAndSurnames();

        Optional<Korisnik> getKorisnikByEmail(String email);
}
