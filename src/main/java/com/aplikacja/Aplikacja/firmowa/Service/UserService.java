package com.aplikacja.Aplikacja.firmowa.Service;

import com.aplikacja.Aplikacja.firmowa.Model.User;
import com.aplikacja.Aplikacja.firmowa.Service.exceptions.UserExistException;
import com.aplikacja.Aplikacja.firmowa.Service.exceptions.UserNotExistException;

import java.util.List;

public interface UserService {
    User save(User user);
    User addNewUser(User user) throws UserExistException;
    void deleteById(Long id) throws UserNotExistException;
    List<User> getAllUsers();
    User findById(Long id) throws UserNotExistException;
}
