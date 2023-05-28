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

    Korisnik registerAdmin(KorisnikDto korisnikDto);

    Korisnik registerKorisnik(KorisnikDto korisnikDto);
        TokenResponse loginAndGetTokens(LoginRequest loginRequest);
        Optional<Korisnik> getKorisnikById(Long id);
        String getUserNameAndSurname(Long userId);
        List<String> getAllUserNamesAndSurnames();


        Korisnik getKorisnikByEmail(String email);

        Long getUserIdByEmail(String email);
        public void posaljiLoginEmail(Korisnik korisnik, String aktivacijskiLink);
    Korisnik findById(Long userId);


       
        String getJobTitleByEmail(String email);

    List<Korisnik> getKorisniciNaCekanju();

        void prihvatiKorisnika(Long korisnikId);

    void posaljiAktivacijskiEmail(Optional<Korisnik> korisnik, String aktivacijskiLink);


    void odbijKorisnika(Long korisnikId, String razlogOdbijanja);

    boolean proveriAktivacijskiLink(Long korisnikId, String timestamp, String hmac);

    void aktivirajKorisnika(Long korisnikId);

}
