package com.main.app.service;

import com.main.app.domain.model.*;
import com.main.app.repository.DocumentRepository;
import com.main.app.repository.KorisnikDocumentRepository;
import com.main.app.repository.KorisnikRepository;
import org.elasticsearch.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KorisnikDocumentServiceImpl implements KorisnikDocumentService{

    private final KorisnikDocumentRepository korisnikDocumentRepository;
    private final KorisnikRepository korisnikRepository;
    private final DocumentRepository documentRepository;

    public KorisnikDocumentServiceImpl(KorisnikDocumentRepository korisnikDocumentRepository, KorisnikRepository korisnikRepository, DocumentRepository documentRepository) {
        this.korisnikDocumentRepository = korisnikDocumentRepository;
        this.korisnikRepository = korisnikRepository;
        this.documentRepository = documentRepository;
    }

    @Override
    public KorisnikDocument addDocumentToKorisnik(Long korisnikId, Long documentId) {
        Optional<Korisnik> korisnikOptional = korisnikRepository.findById(korisnikId);
        if (korisnikOptional.isEmpty()) {
            throw new RuntimeException("Korisnik sa datim ID-em ne postoji.");
        }

        Optional<Document> documentOptional = documentRepository.findById(documentId);
        if (documentOptional.isEmpty()) {
            throw new RuntimeException("Dokument sa datim ID-em ne postoji.");
        }

        Korisnik korisnik = korisnikOptional.get();
        Document document = documentOptional.get();

        KorisnikDocument korisnikDocument = new KorisnikDocument();
        korisnikDocument.setKorisnik(korisnik);
        korisnikDocument.setDocument(document);

        korisnikDocumentRepository.save(korisnikDocument);

        return korisnikDocument;
    }

    @Override
    public List<KorisnikDocument> getAllDocumentsByKorisnikId(Long korisnikId) {
        return korisnikDocumentRepository.findByKorisnikId(korisnikId);
    }

    @Override
    public void deleteDocument(Long documentId) {
        korisnikDocumentRepository.deleteById(documentId);
    }

    @Override
    public byte[] downloadDocument(Long documentId) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));

        return document.getDoc();
    }
}
