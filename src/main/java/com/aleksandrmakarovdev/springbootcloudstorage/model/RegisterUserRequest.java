package com.aleksandrmakarovdev.springbootcloudstorage.model;

import com.aleksandrmakarovdev.springbootcloudstorage.annotations.ValidUsername;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// TODO: check that password contains both letters and numbers
// TODO: check that password and password confirm are equal

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserRequest {

    @NotBlank(message = "Username cannot be empty.")
    @Size(max = 72,message = "Username length must not exceed {max} characters.")
    @ValidUsername
    private String username;

    @NotBlank(message = "Password cannot be empty.")
    @Size(min = 6, max = 254, message = "Password length must be between {min} and {max} characters.")
    private String password;

    @NotBlank(message = "Password Confirm cannot be empty.")
    @Size(min = 6, max = 254, message = "Password Confirm length must be between {min} and {max} characters.")
    private String passwordConfirm;
}
