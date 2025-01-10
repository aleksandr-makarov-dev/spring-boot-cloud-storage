package com.aleksandrmakarovdev.springbootcloudstorage.service;

import com.aleksandrmakarovdev.springbootcloudstorage.entity.User;
import com.aleksandrmakarovdev.springbootcloudstorage.exception.UserExistsException;
import com.aleksandrmakarovdev.springbootcloudstorage.model.RegisterUserRequest;
import com.aleksandrmakarovdev.springbootcloudstorage.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultUserService implements UserService {

    private final PasswordEncoder passwordEncoder;
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
                .build();

        usersRepository.save(user);
    }
}
