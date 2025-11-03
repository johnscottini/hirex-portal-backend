package com.hirex.users;

import com.hirex.users.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByCpf(String cpf);
}
