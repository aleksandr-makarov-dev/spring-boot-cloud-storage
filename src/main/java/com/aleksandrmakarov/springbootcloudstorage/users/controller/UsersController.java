package com.aleksandrmakarov.springbootcloudstorage.users.controller;

import com.aleksandrmakarov.springbootcloudstorage.users.model.LoginUserRequest;
import com.aleksandrmakarov.springbootcloudstorage.users.model.RegisterUserRequest;
import com.aleksandrmakarov.springbootcloudstorage.users.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersController {

    private final UserService userService;

    @GetMapping("register")
    public String register() {
        return "users/register";
    }

    @PostMapping("register")
    public String register(@ModelAttribute @Valid RegisterUserRequest request, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {

            model.addAttribute("errors", bindingResult.getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .toList());

            return "users/register";
        }

        try {
            userService.registerUser(request);
        } catch (Exception e) {
            model.addAttribute("errors", List.of(e.getMessage()));

            return "users/register";
        }

        redirectAttributes.addFlashAttribute("message", "User registered successfully.");

        return "redirect:/users/login";
    }

    @GetMapping("login")
    public String login() {
        return "users/login";
    }

    @PostMapping("login")
    public String login(@ModelAttribute @Valid LoginUserRequest request, BindingResult bindingResult, Model model, HttpServletRequest httpRequest, HttpSession httpSession) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .toList());

            return "users/login";
        }

        try {
            userService.loginUser(request);

            HttpSession session = httpRequest.getSession();
            session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
        } catch (Exception e) {
            model.addAttribute("errors", List.of(e.getMessage()));

            return "users/login";
        }

        return "redirect:/storage";
    }
}
