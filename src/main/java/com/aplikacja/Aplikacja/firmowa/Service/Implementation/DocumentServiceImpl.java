package com.aplikacja.Aplikacja.firmowa.Service.Implementation;

import com.aplikacja.Aplikacja.firmowa.Model.Document;
import com.aplikacja.Aplikacja.firmowa.Repositories.DocumentRepository;
import com.aplikacja.Aplikacja.firmowa.Service.DocumentService;
import com.aplikacja.Aplikacja.firmowa.Service.exceptions.DocumentExistException;
import com.aplikacja.Aplikacja.firmowa.Service.exceptions.DocumentNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;

    @Override
    public Document save(Document document) {
        return documentRepository.save(document);
    }

    @Override
    public Document addNewDocument(Document document) throws DocumentExistException {
        if (documentRepository.findDocumentByTitle(document.getTitle()).isPresent()) {
            throw new DocumentExistException(documentRepository.findDocumentByTitle(document.getTitle()).get().getId());
        }
        return save(document);
    }

    @Override
    public void deleteById(Long id) throws DocumentNotFoundException {
        documentRepository.findById(id);
        documentRepository.deleteById(id);
    }

    @Override
    public List<Document>getAll(){
        return documentRepository.findAll();
    }

    @Override
    public Document findById(Long id){
        return documentRepository.findById(id).orElseThrow(()->new DocumentNotFoundException(id));
    }
}
