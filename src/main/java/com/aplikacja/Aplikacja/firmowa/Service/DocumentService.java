package com.aplikacja.Aplikacja.firmowa.Service;

import com.aplikacja.Aplikacja.firmowa.Model.Document;

import java.util.List;

public interface DocumentService {
    Document save(Document document);
    Document addNewDocument(Document document);
    void deleteById(Long id);
    List<Document> getAll();
    Document findById();
}
