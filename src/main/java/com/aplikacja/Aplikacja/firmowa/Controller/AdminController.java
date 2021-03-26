package com.aplikacja.Aplikacja.firmowa.Controller;

import com.aplikacja.Aplikacja.firmowa.Dto.UserDto;
import com.aplikacja.Aplikacja.firmowa.Load.RequestLogin.Response.ResponseMessage;
import com.aplikacja.Aplikacja.firmowa.Mapper.UserMapper;
import com.aplikacja.Aplikacja.firmowa.Model.User;
import com.aplikacja.Aplikacja.firmowa.Repositories.UserRepository;
import com.aplikacja.Aplikacja.firmowa.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;


@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @PostMapping("/admin/newuser")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createNewUser(@Valid @RequestBody UserDto userDto) {
        if (userRepository.existsByLogin(userDto.getLogin())) {
            return ResponseEntity.badRequest().body(new ResponseMessage("Error: Account with this name is taken"));
        }
        if (userRepository.existsByEmail(userDto.getEmail())) {
            return ResponseEntity.badRequest().body(new ResponseMessage("Error: Account with this email address is in use"));
        }
        userService.addNewUser(userMapper.mapToUser(userDto));

        return ResponseEntity.ok(new ResponseMessage("New account account is created successfully"));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserDto getUser(@PathVariable Long id) {
        return userMapper.mapToUserDto(userService.findById(id));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserDto>getAllUsers(){
        return userMapper.mapToUserDtoList(userService.getAllUsers());
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(@PathVariable Long id){
        userService.deleteById(id);
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserDto updateUserInformation(@PathVariable Long id, @RequestBody UserDto userDto){
        User user= userService.findById(id);
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setSignUpDate(userDto.getSignUpDate());
        return userMapper.mapToUserDto(userService.save(user));
    }
}
