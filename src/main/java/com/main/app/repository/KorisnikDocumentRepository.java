package com.main.app.repository;

import com.main.app.domain.model.Document;
import com.main.app.domain.model.Korisnik;
import com.main.app.domain.model.KorisnikDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KorisnikDocumentRepository extends JpaRepository<KorisnikDocument, Long> {
    KorisnikDocument findByDocumentAndKorisnik(Document document, Korisnik korisnik);

    List<KorisnikDocument> findByKorisnikId(Long korisnikId);
}
