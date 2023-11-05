package com.aptech.shop.demo.request;

import com.aptech.shop.demo.Validate.UniqueEmail;
import com.aptech.shop.demo.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;
import java.util.Set;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "Username shouldn't be null")
    private String name;
    @Pattern(regexp = "^\\d{10}$", message = "Invalid mobile number entered")
    private String phone;
    @Email(message = "Invalid email address")
    @UniqueEmail(message = "Email is already")
    private String email;
    @NotBlank(message = "Password shouldn't be null")
    private String password;
    private List<String> roles;
}
