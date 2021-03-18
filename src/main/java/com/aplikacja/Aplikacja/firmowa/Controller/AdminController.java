package com.aplikacja.Aplikacja.firmowa.Controller;

import com.aplikacja.Aplikacja.firmowa.Model.User;
import com.aplikacja.Aplikacja.firmowa.Service.DocumentService;
import com.aplikacja.Aplikacja.firmowa.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class AdminController {
    @Autowired(required = true)
    UserService userService;
    @Autowired
    DocumentService documentService;



    @GetMapping("{user_id}/deleteuser")
    public String deleteUserById(@PathVariable(name = "user_id")long user_id, Model model){
        this.userService.deleteById(user_id);
        return "redirect:/user-user-list";
    }
    @GetMapping("/user-list")
    public String showUserList(Model model){
        List<User> userList= userService.getAllUsers();
        model.addAttribute("userList",userList);
        return "user/user-list";
    }
    @PostMapping("/admin/saveuser")
    public String addNewUser(@ModelAttribute("user")User user){
        userService.save(user);
        return "redirect:/user/user-list";
    }
}
