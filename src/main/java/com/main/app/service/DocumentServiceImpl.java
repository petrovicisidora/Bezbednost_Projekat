package com.main.app.service;

import com.main.app.domain.model.Document;
import com.main.app.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocumentServiceImpl implements DocumentService{

    @Autowired
    DocumentRepository documentRepository;

    public DocumentServiceImpl(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    @Override
    public Document createDocument(String name, byte[] doc) {
        Document document = new Document();
        document.setName(name);
        document.setDoc(doc);
        return documentRepository.save(document);
    }
}
