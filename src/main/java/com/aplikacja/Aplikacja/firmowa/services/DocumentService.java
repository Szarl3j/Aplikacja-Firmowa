package com.aplikacja.Aplikacja.firmowa.services;

import com.aplikacja.Aplikacja.firmowa.model.Document;
import com.aplikacja.Aplikacja.firmowa.services.exceptions.DocumentExistException;

import java.util.List;

public interface DocumentService {
    Document save(Document document);
    Document addNewDocument(Document document) throws DocumentExistException;
    void deleteById(Long id);
    List<Document> getAll();
    Document findById(Long id);
}
