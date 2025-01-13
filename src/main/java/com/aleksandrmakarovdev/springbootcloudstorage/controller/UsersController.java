package com.aleksandrmakarovdev.springbootcloudstorage.controller;

import com.aleksandrmakarovdev.springbootcloudstorage.exception.UserExistsException;
import com.aleksandrmakarovdev.springbootcloudstorage.model.LoginUserRequest;
import com.aleksandrmakarovdev.springbootcloudstorage.model.RegisterUserRequest;
import com.aleksandrmakarovdev.springbootcloudstorage.security.WebUserDetails;
import com.aleksandrmakarovdev.springbootcloudstorage.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
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

import java.security.Principal;
import java.util.List;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersController {

    private final UserService userService;

    @GetMapping("register")
    public String register(Model model, @ModelAttribute("request") RegisterUserRequest registerUserRequest) {

        model.addAttribute("user", new RegisterUserRequest());

        return "/users/register";
    }

    @PostMapping("register")
    public String register(Model model, @ModelAttribute("request") @Valid RegisterUserRequest request, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult
                    .getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .toList());
            model.addAttribute("user", new RegisterUserRequest());

            return "users/register";
        }

        try {
            userService.registerUser(request);
            redirectAttributes.addFlashAttribute("message", "User registered successfully");
            return "redirect:/users/login";
        } catch (UserExistsException e) {
            model.addAttribute("errors", List.of(e.getMessage()));
            return "users/register";
        }
    }

    @GetMapping("login")
    public String login(Model model, @ModelAttribute("request") LoginUserRequest loginUserRequest) {
        model.addAttribute("user", new LoginUserRequest());

        return "users/login";
    }

    @PostMapping("login")
    public String login(Model model, @ModelAttribute("request") @Valid LoginUserRequest request, BindingResult bindingResult, HttpServletRequest httpRequest) {

        if (bindingResult.hasErrors()) {

            model.addAttribute("errors", bindingResult
                    .getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .toList());

            model.addAttribute("user", new LoginUserRequest());

            return "users/login";
        }

        try {
            Authentication authentication = userService.loginUser(request);

            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(authentication);

            HttpSession session = httpRequest.getSession();
            session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, securityContext);

        } catch (Exception e) {

            model.addAttribute("errors", List.of(e.getMessage()));
            model.addAttribute("user", new LoginUserRequest());

            return "users/login";
        }

        return "redirect:/users/me";
    }

    @GetMapping("me")
    public String me(@AuthenticationPrincipal WebUserDetails userDetails, Model model) {

        if (userDetails != null) {
            model.addAttribute("user", userDetails.getUsername());
        } else {
            model.addAttribute("user", "anonymous");
        }

        return "users/me";
    }

    @GetMapping("logout")
    public String logout() {
        return "users/logout";
    }
}
