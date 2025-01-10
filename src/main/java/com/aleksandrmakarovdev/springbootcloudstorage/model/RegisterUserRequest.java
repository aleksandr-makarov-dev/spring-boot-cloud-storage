package com.aleksandrmakarovdev.springbootcloudstorage.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterUserRequest {

    @NotBlank(message = "Username cannot be empty.")
    @Size(max = 72,message = "Username length must not exceed {max} characters.")
    private String username;

    @NotBlank(message = "Password cannot be empty.")
    @Size(min = 6, max = 254, message = "Password length must be between {min} and {max} characters.")
    private String password;

    @NotBlank(message = "Password Confirm cannot be empty.")
    @Size(min = 6, max = 254, message = "Password Confirm length must be between {min} and {max} characters.")
    private String passwordConfirm;
}
