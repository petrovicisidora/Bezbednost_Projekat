package com.main.app.service;
import com.main.app.domain.dto.KorisnikDto;
import com.main.app.domain.model.Korisnik;

import java.util.Optional;

public interface CurrentKorisnikService {

    Optional<Korisnik> getCurrentKorisnik();

    KorisnikDto getCurrentKorisnikDto();
}
