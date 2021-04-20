package com.aplikacja.Aplikacja.firmowa.repositories;

import com.aplikacja.Aplikacja.firmowa.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DocumentRepository extends JpaRepository<Document,Long> {

    @Override
    <S extends  Document>S save(S document);

    @Override
    void  deleteById(Long id);

    @Override
    List<Document> findAll();

    @Override
    Optional <Document> findById(Long id);

    Optional <Document> findDocumentByTitle(String title);

}
