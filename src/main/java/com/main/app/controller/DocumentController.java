package com.main.app.controller;

import com.main.app.domain.model.Document;
import com.main.app.service.DocumentService;
import com.main.app.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/document")
public class DocumentController {

    private DocumentService documentService;

    @Autowired
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping("/create")
    public ResponseEntity<Document> createDocument(@RequestParam("name") String name, @RequestParam("doc") MultipartFile doc) {
        if (!doc.isEmpty()) {
            byte[] docBytes;
            try {
                docBytes = doc.getBytes();
            } catch (IOException e) {

                e.printStackTrace();
                return ResponseEntity.badRequest().build();
            }
            Document createdDocument = documentService.createDocument(name, docBytes);
            return ResponseEntity.ok(createdDocument);
        } else {

            return ResponseEntity.badRequest().build();
        }
    }
}
