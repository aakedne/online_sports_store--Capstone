package com.app.onlinesportstore.controller;

import com.app.onlinesportstore.model.User;
import com.app.onlinesportstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Optional;


@Controller
public class AuthenticationController {

    @Autowired
    UserService userService;

    /**
     * This will handle the request to show the login page.
     * While login form submission will be handled by spring security itself.
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * If user enters wrong credentials then this controller will handle that request.
     */
    @GetMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("error", true);
        return "login";
    }

    /**
     * This will show the form for the registration.
     */
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    /**
     * It will handle the form submission of the registration page.
     */
    @PostMapping("/register")
    public String registerForSubmission(@Valid User user, BindingResult result, Model model) {
        if (!result.hasErrors()) {
            Optional<User> existingUser = userService.getUserByUsername(user.getUsername());
            if (existingUser.isPresent()) {
                model.addAttribute("userAlreadyExists", true);
            } else {
                userService.save(user);
                model.addAttribute("successfully", true);
                model.addAttribute("user", new User());
            }
        }
        return "register";
    }
}
