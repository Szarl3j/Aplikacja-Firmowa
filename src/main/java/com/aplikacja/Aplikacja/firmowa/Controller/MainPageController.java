package com.aplikacja.Aplikacja.firmowa.Controller;

import com.aplikacja.Aplikacja.firmowa.Repositories.UserRepository;
import com.aplikacja.Aplikacja.firmowa.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class MainPageController {
    final UserService userService;

    public MainPageController(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    UserRepository userRepository;


    }



//    @GetMapping("/createAdminAccount")
//    public String createAdminAccount() {
//        return "createAdminAccount";
//    }
//
//    @GetMapping("/login")
//    public String login() {
//        return "login";
//    }
//    @GetMapping("/")
//    public String afterLoginRedirect() {
//        return "redirect:/user";
//    }
//}
