package com.aplikacja.Aplikacja.firmowa.Service;

import com.aplikacja.Aplikacja.firmowa.Model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    List<User> getAllUsers();
    void saveUser(User user);
    User getUserById(long id);
    void deleteUserById(long id);


}
