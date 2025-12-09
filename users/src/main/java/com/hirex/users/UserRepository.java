package com.hirex.users;

import com.hirex.users.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByCpf(String cpf);
    Optional<User> findByKeycloakId(String keycloakId);
}
