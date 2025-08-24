package dto;

import com.hirex.portal.domain.enums.Gender;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String fullName;
    private String email;
    private String cpf;
    private Gender gender;
    private LocalDate birthDate;
}
