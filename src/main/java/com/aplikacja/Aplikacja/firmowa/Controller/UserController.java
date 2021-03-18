package com.aplikacja.Aplikacja.firmowa.Controller;


import com.aplikacja.Aplikacja.firmowa.Load.RequestLogin.RequiredLogin;
import com.aplikacja.Aplikacja.firmowa.Mapper.UserMapper;
import com.aplikacja.Aplikacja.firmowa.Repositories.UserRepository;
import com.aplikacja.Aplikacja.firmowa.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;


import javax.validation.Valid;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/authorize/login")
    public ResponseEntity<?> authorizeUser(@Valid @RequestBody RequiredLogin requiredLogin) {
        UsernamePasswordAuthenticationToken loginAndPasswordAuth =
                new UsernamePasswordAuthenticationToken(requiredLogin.getLogin(),
                        requiredLogin.getPassword());

        Authentication authentication = authenticationManager
                .authenticate(loginAndPasswordAuth);

        SecurityContextHolder.getContext().setAuthentication(authentication);



    }


}
