package com.main.app.service;

import com.main.app.domain.dto.KorisnikDto;
import com.main.app.domain.model.Korisnik;

public interface KorisnikService {

        Korisnik registerKorisnik(KorisnikDto korisnikDto);

}
