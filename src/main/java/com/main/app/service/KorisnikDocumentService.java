package com.main.app.service;

import com.main.app.domain.model.Document;
import com.main.app.domain.model.Korisnik;
import com.main.app.domain.model.KorisnikDocument;
import com.main.app.domain.model.KorisnikSkill;

import java.util.List;

public interface KorisnikDocumentService {

    KorisnikDocument addDocumentToKorisnik(Long korisnikId, Long documentId);

    List<KorisnikDocument> getAllDocumentsByKorisnikId(Long korisnikId);


    void deleteDocument(Long documentId);

    byte[] downloadDocument(Long documentId);
}
