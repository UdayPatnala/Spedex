package com.spedex.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SignUpRequestDto {
    @NotBlank(message = "Name is required")
    public String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    public String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    public String password;

    public Boolean isMinor = false;
    public Integer age = 18;
    public String guardianEmail;
    public String guardianName;
    public Boolean analyticsConsent = false;
    public Boolean marketingConsent = false;
    public Boolean locationConsent = false;
    public Boolean aiConsent = false;
}
