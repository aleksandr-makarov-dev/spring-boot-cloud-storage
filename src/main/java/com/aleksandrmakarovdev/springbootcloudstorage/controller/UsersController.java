package com.aleksandrmakarovdev.springbootcloudstorage.controller;

import com.aleksandrmakarovdev.springbootcloudstorage.model.RegisterUserRequest;
import com.aleksandrmakarovdev.springbootcloudstorage.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UsersController {

    private UserService userService;

    @GetMapping("register")
    public String register() {
        return "/users/register";
    }

    @PostMapping("register")
    public String register(@ModelAttribute Model model, @Valid RegisterUserRequest request, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult
                    .getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage));
        }

        userService.registerUser(request);

        model.addAttribute("message", "User registered successfully");

        return "/users/register";
    }
}
