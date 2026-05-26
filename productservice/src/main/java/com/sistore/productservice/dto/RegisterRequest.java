package com.sistore.productservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class RegisterRequest {
    @NotBlank
    private String name;

    @Email
    private String email;

    @Size(min = 6)
    private String password;
}
