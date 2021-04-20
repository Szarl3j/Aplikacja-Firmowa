package com.aplikacja.Aplikacja.firmowa.controller;

import com.aplikacja.Aplikacja.firmowa.dto.DocumentDto;
import com.aplikacja.Aplikacja.firmowa.mapper.DocumentMapper;
import com.aplikacja.Aplikacja.firmowa.model.Document;
import com.aplikacja.Aplikacja.firmowa.services.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/document")
public class DocumentController {

    private final DocumentService documentService;
    private final DocumentMapper documentMapper;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Long addDocument(@RequestBody DocumentDto documentDto) {
        return documentService.addNewDocument(documentMapper
                .mapToDocument(documentDto)).getId();
    }

    @GetMapping("{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public DocumentDto getDocument(@PathVariable Long id) {
        return documentMapper.mapToDocumentDto(documentService.findById(id));
    }

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<DocumentDto> getAll() {
        return documentMapper.mapToDocumentDtoList(documentService.getAll());
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteDocument(@PathVariable Long id) {
        documentService.deleteById(id);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public DocumentDto editDocument(@PathVariable Long id, @RequestBody DocumentDto documentDto) {
        Document document = documentService.findById(id);
        document.setTitle(documentDto.getTitle());
        document.setDescription(documentDto.getDescription());
        return documentMapper.mapToDocumentDto(documentService.save(document));

    }


}
