package com.aplikacja.Aplikacja.firmowa.Service.exceptions;

public class DocumentNotFoundException extends  RuntimeException {

public DocumentNotFoundException(Long id){super("Document not found in database, document id"
+id);}
}
