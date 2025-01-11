package com.aleksandrmakarovdev.springbootcloudstorage.model;

import com.aleksandrmakarovdev.springbootcloudstorage.annotations.ValidUsername;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginUserRequest {

    @NotBlank(message = "Username cannot be empty.")
    @Size(max = 72,message = "Username length must not exceed {max} characters.")
    @ValidUsername
    private String username;

    @NotBlank(message = "Password cannot be empty.")
    @Size(min = 6, max = 254, message = "Password length must be between {min} and {max} characters.")
    private String password;
}
