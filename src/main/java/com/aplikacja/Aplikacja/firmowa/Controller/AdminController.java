package com.aplikacja.Aplikacja.firmowa.Controller;

import com.aplikacja.Aplikacja.firmowa.Model.User;
import com.aplikacja.Aplikacja.firmowa.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Controller
public class AdminController {
    @Autowired(required = true)
    UserService userService;

    //ADMIN ROLE ONLY
    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@PathVariable(name = "user_id")long user_id, Model model){
       User user= userService.getUserById(user_id);
      // model.addAtribute("user", user);
       return "user/update_user";
    }
//   //IN FUTURE
//    @PostMapping("admin/uploadFile")
//    public ResponseEntity uploadToLocalFile(RequestParam("file")MultipartFile file){
//        Path path= Paths.get(fileBasePath + fileName);
//        try{
//Files.copy(file.getInputStream(),path, StandardCopyOption.REPLACE_EXISTING);
//        }catch(IOException e){
//            e.printStackTrace();
//        }
    //}
    @GetMapping("{id}/deleteUser")
    public String deleteUserById(@PathVariable(name = "user_id")long user_id, Model model){
        this.userService.deleteUserById(user_id);
        return "redirect:/user-user-list";
    }
    @GetMapping("/user-list")
    public String showUserList(@PathVariable(name = "user_id") long user_id, Model model){
        List<User> userList= userService.getAllUsers();
        model.addAttribute("userList",userList);
        return "user/user-list";
    }
    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute("user")User user){
        userService.saveUser(user);
        return "redirect:/user/user-list";
    }
}
