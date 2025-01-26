package com.aleksandrmakarov.springbootcloudstorage.users.service;

import com.aleksandrmakarov.springbootcloudstorage.users.entity.User;
import com.aleksandrmakarov.springbootcloudstorage.users.model.LoginUserRequest;
import com.aleksandrmakarov.springbootcloudstorage.users.model.RegisterUserRequest;
import com.aleksandrmakarov.springbootcloudstorage.users.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class DefaultUserService implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    public void registerUser(RegisterUserRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("User already exists");
        }

        User user = User.builder()
                .username(request.getUsername())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .createdAt(new Date())
                .build();

        userRepository.save(user);

        log.info("User created: {}", user.getUsername());
    }

    public void loginUser(LoginUserRequest request) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());

        Authentication authentication = authenticationManager.authenticate(token);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
