package com.aplikacja.Aplikacja.firmowa.Mapper;

import com.aplikacja.Aplikacja.firmowa.Dto.UserDto;
import com.aplikacja.Aplikacja.firmowa.Model.Role;
import com.aplikacja.Aplikacja.firmowa.Model.User;
import com.aplikacja.Aplikacja.firmowa.Model.ERoles;
import com.aplikacja.Aplikacja.firmowa.Repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final PasswordEncoder encoder;
    private final RoleRepository roleRepository;

    public User mapToUser(UserDto userDto) {
        Set<String> strRoles = userDto.getRole();
        Set<Role> roles = new HashSet<>();

        checkRoles();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERoles.USER_ROLE)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERoles.ADMIN_ROLE)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                }
            });
        }
            return User.builder()
                    .login(userDto.getLogin())
                    .firstName(userDto.getFirstName())
                    .lastName(userDto.getLastName())
                    .email(userDto.getEmail())
                    .password(encoder.encode(userDto.getPassword()))
                    .role(roles)
                    .build();
    }

    private void checkRoles() {

        if(!(roleRepository.findByName(ERoles.USER_ROLE).isPresent())) {
            Role role = new Role(ERoles.USER_ROLE);
            roleRepository.save(role);

        }
        if(!(roleRepository.findByName(ERoles.ADMIN_ROLE).isPresent())) {
            Role role = new Role(ERoles.ADMIN_ROLE);
            roleRepository.save(role);
        }
    }
}



