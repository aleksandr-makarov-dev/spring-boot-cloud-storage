package com.aleksandrmakarovdev.springbootcloudstorage.service;

import com.aleksandrmakarovdev.springbootcloudstorage.model.LoginUserRequest;
import com.aleksandrmakarovdev.springbootcloudstorage.model.RegisterUserRequest;

public interface UserService {

    void registerUser(RegisterUserRequest request);
    void loginUser(LoginUserRequest request);
}
