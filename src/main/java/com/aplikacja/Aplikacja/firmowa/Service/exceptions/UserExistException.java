package com.aplikacja.Aplikacja.firmowa.Service.exceptions;

 public class UserExistException extends RuntimeException {

    public UserExistException(Long id) {
        super("User already exist, user id: " + id);
    }
}
