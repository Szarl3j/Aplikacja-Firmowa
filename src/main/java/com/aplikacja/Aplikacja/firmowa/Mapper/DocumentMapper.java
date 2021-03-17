package com.aplikacja.Aplikacja.firmowa.Mapper;

import com.aplikacja.Aplikacja.firmowa.Dto.DocumentDto;
import com.aplikacja.Aplikacja.firmowa.Model.Document;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DocumentMapper {
    public Document mapToDocument(DocumentDto documentDto){
        return Document.builder()
                .title(documentDto.getTitle())
                .description(documentDto.getDescription())
                .build();

    }
    public DocumentDto mapToDocumentDto(Document document){
        return new DocumentDto(document.getTitle(),document.getDescription());
    }
    public List<DocumentDto>mapToDocumentDtoList(List<Document>documentList){
        return documentList.stream()
                .map(this::mapToDocumentDto)
                .collect(Collectors.toList());
    }
}
