package com.aleksandrmakarovdev.springbootcloudstorage.service;

import com.aleksandrmakarovdev.springbootcloudstorage.entity.User;
import com.aleksandrmakarovdev.springbootcloudstorage.exception.UserExistsException;
import com.aleksandrmakarovdev.springbootcloudstorage.model.LoginUserRequest;
import com.aleksandrmakarovdev.springbootcloudstorage.model.RegisterUserRequest;
import com.aleksandrmakarovdev.springbootcloudstorage.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class DefaultUserService implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UsersRepository usersRepository;

    @Override
    public void registerUser(RegisterUserRequest request) {

        if (usersRepository.existsByUsername(request.getUsername())) {
            throw new UserExistsException("Username already exists");
        }

        String passwordHash = passwordEncoder.encode(request.getPassword());

        User user = User.builder()
                .username(request.getUsername())
                .passwordHash(passwordHash)
                .createdAt(Date.from(Instant.now()))
                .build();

        usersRepository.save(user);
    }

    @Override
    public void loginUser(LoginUserRequest request) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());

        Authentication authentication = authenticationManager
                .authenticate(authenticationToken);

        // TODO: do something...
    }
}
