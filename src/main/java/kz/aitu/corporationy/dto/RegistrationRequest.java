package kz.aitu.corporationy.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegistrationRequest(@Size(min = 4, message = "Full name must be more than 4 characters")
                                  String fullName,
                                  @Size(min = 4, message = "Username must be more than 4 characters")
                                  String username,
                                  @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$",
                                          message = "Password must be at least 8 characters long and include digits, uppercase and lowercase letters, and a special character")
                                  String password) {
}