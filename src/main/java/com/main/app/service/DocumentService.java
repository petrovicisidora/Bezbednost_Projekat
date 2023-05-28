package com.main.app.service;

import com.main.app.domain.model.Document;

public interface DocumentService {
    Document createDocument(String name, byte[] doc);
}
