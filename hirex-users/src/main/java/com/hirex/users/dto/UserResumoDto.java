package com.hirex.users.dto;

import lombok.Data;

@Data
public class UserResumoDto {
    private Long id;
    private String username;
    private String fullName;
    private String email;
    private String cpf;
}
