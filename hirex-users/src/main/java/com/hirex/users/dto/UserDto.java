package com.hirex.users.dto;

import com.hirex.users.domain.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDto {
    private Long id;

    @NotBlank
    private String username;
    private String fullName;

    private String password;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Max(16)
    private String cpf;
    private Gender gender;
    private LocalDate birthDate;
}
