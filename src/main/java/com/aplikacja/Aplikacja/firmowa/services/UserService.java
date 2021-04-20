package com.aplikacja.Aplikacja.firmowa.services;

import com.aplikacja.Aplikacja.firmowa.model.User;
import com.aplikacja.Aplikacja.firmowa.services.exceptions.UserExistException;
import com.aplikacja.Aplikacja.firmowa.services.exceptions.UserNotExistException;

import java.util.List;


public interface UserService {
    User save(User user);
    User addNewUser(User user) throws UserExistException;
    void deleteById(Long id) throws UserNotExistException;
    List<User> getAllUsers();
    User findById(Long id) throws UserNotExistException;



}
