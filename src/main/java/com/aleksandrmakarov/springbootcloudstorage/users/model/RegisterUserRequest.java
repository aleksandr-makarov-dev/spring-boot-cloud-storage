package com.aleksandrmakarov.springbootcloudstorage.users.model;

import com.aleksandrmakarov.springbootcloudstorage.users.annotations.FieldMatch;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldMatch({"password","confirmPassword"})
public class RegisterUserRequest {

    @NotBlank(message = "Username cannot be empty.")
    @Size(min = 4, max = 72, message = "Username length must be between {min} and {max} characters.")
    private String username;

    @NotBlank(message = "Password cannot be empty.")
    @Size(min = 6, max = 72, message = "Password length must be between {min} and {max} characters.")
    private String password;

    @NotBlank(message = "Password Confirm cannot be empty.")
    @Size(min = 6, max = 72, message = "Password Confirm length must be between {min} and {max} characters.")
    private String confirmPassword;
}
