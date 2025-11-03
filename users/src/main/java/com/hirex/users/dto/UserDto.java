package com.hirex.users.dto;

import com.hirex.users.domain.enums.Gender;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDto {
    private Long id;

    private String username;
    private String fullName;

    private String password;

    private String email;

    private String cpf;
    private Gender gender;
    private LocalDate birthDate;
}
