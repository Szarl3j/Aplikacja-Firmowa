package com.aplikacja.Aplikacja.firmowa.services.exceptions;

public class DocumentNotFoundException extends  RuntimeException {

public DocumentNotFoundException(Long id){super("Document not found in database, document id"
+id);}
}
