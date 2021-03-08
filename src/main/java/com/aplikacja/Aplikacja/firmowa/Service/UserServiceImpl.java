package com.aplikacja.Aplikacja.firmowa.Service;

import com.aplikacja.Aplikacja.firmowa.Model.Role;
import com.aplikacja.Aplikacja.firmowa.Model.User;
import com.aplikacja.Aplikacja.firmowa.Repositories.UserRepository;
import com.aplikacja.Aplikacja.firmowa.Dto.dto.UserRegistrationDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {




    // constructor based injection for UserRepository - Recommend
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public User getUserById(long id) {
        Optional<User> optional = userRepository.findById(id);
        User user = null;
        if (optional.isPresent()) {
            user = optional.get();
        } else {
            throw new RuntimeException("User not found for id :: " + id);
        }
        return user;
    }

    @Override
    public void deleteUserById(long id) {
        this.userRepository.deleteById(id);
    }

    @Override
    public User save(UserRegistrationDto registrationDto) {
        return null;
    }


//    @Override
//    public User save(UserRegistrationDto registrationDto) {
//        User user = new User(registrationDto.getFirst_name(),
//                registrationDto.getLast_name(),
//                registrationDto.getEmail(),
//                //encoding password
//                bCryptPasswordEncoder.encode(registrationDto.getPassword()), Arrays.asList(new Role("ROLE_USER")));
//
//        return userRepository.save(user);
//    }

//    @Override
//    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
//        User user = userRepository.findByEmail(s);
//        if (user == null) {
//            throw new UsernameNotFoundException("Wrong user name or password.");
//        }
//        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), mapRolesToAuthorities(user.getUser_roles()));
//    }

    // method that mapps roles with authorities
    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getRole_name())).collect(Collectors.toList());
    }

//    @Override
//    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
//        return null;
//    }
}
