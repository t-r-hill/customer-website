package co.LabsProjects.customerwebsite.controller;

import co.LabsProjects.customerwebsite.model.User;
import co.LabsProjects.customerwebsite.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/register")
    public String showRegistrationPage(Model model){
        User user = User.builder().build();
        model.addAttribute(user);
        return "registration";
    }

    @PostMapping("register")
    public String registerUser(@ModelAttribute User user){
        userService.createNewUser(user);
        return "index";
    }
}
