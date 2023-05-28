package com.main.app.controller;

import com.main.app.domain.model.Document;
import com.main.app.domain.model.Korisnik;
import com.main.app.domain.model.KorisnikDocument;
import com.main.app.domain.model.KorisnikSkill;
import com.main.app.service.DocumentService;
import com.main.app.service.KorisnikDocumentService;
import com.main.app.service.KorisnikService;
import com.main.app.service.KorisnikSkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/korisnikdocument")
public class KorisnikDocumentController {

    private KorisnikDocumentService korisnikDocumentService;
    private KorisnikService korisnikService;
    private DocumentService documentService;

    @Autowired
    public KorisnikDocumentController(KorisnikDocumentService korisnikDocumentService) {
        this.korisnikDocumentService = korisnikDocumentService;
    }

    @PostMapping("/korisnik/{korisnikId}/document/{documentId}")
    public ResponseEntity<KorisnikDocument> addDocumentToKorisnik(@PathVariable Long korisnikId, @PathVariable Long documentId) {
        KorisnikDocument korisnikDocument = korisnikDocumentService.addDocumentToKorisnik(korisnikId, documentId);
        return ResponseEntity.status(HttpStatus.CREATED).body(korisnikDocument);
    }

    @GetMapping("/getAllUserDocuments/{id}")
    public ResponseEntity<List<KorisnikDocument>> getAllDocumentsByKorisnikId(@PathVariable("id") Long korisnikId) {
        List<KorisnikDocument> documents = korisnikDocumentService.getAllDocumentsByKorisnikId(korisnikId);
        return new ResponseEntity<>(documents, HttpStatus.OK);
    }

    @DeleteMapping("/deleteDocument/{documentId}")
    public ResponseEntity<?> deleteDocument(@PathVariable Long documentId) {
        korisnikDocumentService.deleteDocument(documentId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/document/download/{documentId}")
    public ResponseEntity<Resource> downloadDocument(@PathVariable Long documentId) {
        byte[] documentBytes = korisnikDocumentService.downloadDocument(documentId);

        ByteArrayResource resource = new ByteArrayResource(documentBytes);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=document.pdf")
                .contentLength(documentBytes.length)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
