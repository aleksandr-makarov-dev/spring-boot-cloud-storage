package com.aleksandrmakarov.springbootcloudstorage.users.service;

import com.aleksandrmakarov.springbootcloudstorage.users.model.LoginUserRequest;
import com.aleksandrmakarov.springbootcloudstorage.users.model.RegisterUserRequest;

public interface UserService {

    void registerUser(RegisterUserRequest request);

    void loginUser(LoginUserRequest request);
}
