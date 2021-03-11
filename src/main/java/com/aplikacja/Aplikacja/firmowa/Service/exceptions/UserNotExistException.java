package com.aplikacja.Aplikacja.firmowa.Service.exceptions;

public class UserNotExistException extends RuntimeException{

    public UserNotExistException(Long id) {
        super("User not exist, user id: " + id);
    }
}
