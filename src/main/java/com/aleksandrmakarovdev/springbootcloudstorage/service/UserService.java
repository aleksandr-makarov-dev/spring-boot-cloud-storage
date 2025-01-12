package com.aleksandrmakarovdev.springbootcloudstorage.service;

import com.aleksandrmakarovdev.springbootcloudstorage.model.LoginUserRequest;
import com.aleksandrmakarovdev.springbootcloudstorage.model.RegisterUserRequest;
import org.springframework.security.core.Authentication;

public interface UserService {

    void registerUser(RegisterUserRequest request);
    Authentication loginUser(LoginUserRequest request);
}
