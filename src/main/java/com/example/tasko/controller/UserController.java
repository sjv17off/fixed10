package com.example.tasko.controller;

import com.example.tasko.model.User;
import com.example.tasko.model.UserRole;
import com.example.tasko.service.EnterpriseService;
import com.example.tasko.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.validation.Valid;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EnterpriseService enterpriseService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("enterprises", enterpriseService.getAllEnterprises());
        model.addAttribute("roles", UserRole.values());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("enterprises", enterpriseService.getAllEnterprises());
            model.addAttribute("roles", UserRole.values());
            return "register";
        }
        try {
            userService.createUser(user);
        } catch (Exception e) {
            bindingResult.rejectValue("username", "error.user", "An account already exists for this username.");
            model.addAttribute("enterprises", enterpriseService.getAllEnterprises());
            model.addAttribute("roles", UserRole.values());
            return "register";
        }
        return "redirect:/login?registered";
    }

    @GetMapping("/profile")
    public String showUserProfile(Model model, Authentication authentication) {
        String username = authentication.getName();
        User user = userService.getUserByUsername(username);
        model.addAttribute("user", user);
        return "profile";
    }
}