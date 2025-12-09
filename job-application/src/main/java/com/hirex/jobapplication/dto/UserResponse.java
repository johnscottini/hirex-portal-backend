package com.hirex.jobapplication.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserResponse {
    private Long id;

    private String username;
    private String fullName;
    private String email;
    private String cpf;
    private LocalDate birthDate;
    private Boolean enabled;
}
