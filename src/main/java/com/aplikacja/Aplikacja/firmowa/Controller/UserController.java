package com.aplikacja.Aplikacja.firmowa.Controller;


import com.aplikacja.Aplikacja.firmowa.Dto.UserDto;
import com.aplikacja.Aplikacja.firmowa.Load.RequestLogin.RequiredLogin;
import com.aplikacja.Aplikacja.firmowa.Load.RequestLogin.Response.JWebTokenResponse;
import com.aplikacja.Aplikacja.firmowa.Load.RequestLogin.Response.ResponseMessage;
import com.aplikacja.Aplikacja.firmowa.Mapper.UserMapper;
import com.aplikacja.Aplikacja.firmowa.Repositories.UserRepository;
import com.aplikacja.Aplikacja.firmowa.Service.UserService;
import com.aplikacja.Aplikacja.firmowa.security.JWebToken.JWebTokenUtils;
import com.aplikacja.Aplikacja.firmowa.security.service.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;


import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JWebTokenUtils jWebTokenUtils;

    @PostMapping("/authorize/login")
    public ResponseEntity<?> authorizeUser(@Valid @RequestBody RequiredLogin requiredLogin) {
        UsernamePasswordAuthenticationToken loginAndPasswordAuth =
                new UsernamePasswordAuthenticationToken(requiredLogin.getLogin(),
                        requiredLogin.getPassword());

        Authentication authentication = authenticationManager
                .authenticate(loginAndPasswordAuth);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jWebToken = jWebTokenUtils.generateJWebToken(authentication);

        UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();
        List<String> role = userDetailsImpl.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toList());

        return ResponseEntity.ok(new JWebTokenResponse(jWebToken, userDetailsImpl.getId(),
                userDetailsImpl.getUsername(), userDetailsImpl.getEmail(), role));
    }

    @PostMapping("/authorize/register")
    public ResponseEntity<?> registerNewAdmin(@Valid @RequestBody UserDto userDto) {
        if (userRepository.existsByLogin(userDto.getLogin())) {
            return ResponseEntity.badRequest().body(new ResponseMessage("Error: This username is taken"));
        }
        if (userRepository.existsByEmail(userDto.getEmail())) {
            return ResponseEntity.badRequest().body(new ResponseMessage("Error: this email address is in use"));
        }
        userService.addNewUser(userMapper.mapToUser(userDto));

        return ResponseEntity.ok(new ResponseMessage("New admin account is created successfully"));
    }

}
