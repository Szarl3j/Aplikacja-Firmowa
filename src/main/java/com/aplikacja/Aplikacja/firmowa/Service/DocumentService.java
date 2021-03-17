package com.aplikacja.Aplikacja.firmowa.Service;

import com.aplikacja.Aplikacja.firmowa.Model.Document;
import com.aplikacja.Aplikacja.firmowa.Service.exceptions.DocumentExistException;

import java.util.List;

public interface DocumentService {
    Document save(Document document);
    Document addNewDocument(Document document) throws DocumentExistException;
    void deleteById(Long id);
    List<Document> getAll();
    Document findById(Long id);
}
