package com.aplikacja.Aplikacja.firmowa.Service.exceptions;

public class DocumentExistException extends RuntimeException{

    public DocumentExistException(Long id){super ("Document exist in database, document id"
    + id);}
}
