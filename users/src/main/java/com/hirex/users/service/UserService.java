package com.hirex.users.service;

import com.hirex.common.utils.PageUtils;
import com.hirex.users.UserMapper;
import com.hirex.users.UserRepository;
import com.hirex.users.domain.User;
import com.hirex.users.dto.UserDto;
import com.hirex.users.dto.UserResumoDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public List<UserResumoDto> findAll(Integer pageIndex, Integer pageSize, String sortField) {
        log.info("Listing all users:");
        var users = userRepository.findAll(PageUtils.page(pageIndex, pageSize, sortField));

        return userMapper.toUsersResumoDto(users);
    }

    public UserDto get(Long id) {
        var users = userRepository.findById(id).orElse(null);
        return userMapper.toUserDto(users);
    }

    public UserDto save(UserDto userDto) {
        var isCpfExisting = userRepository.existsByCpf(userDto.getCpf());
        if(isCpfExisting) {
            log.error("User with CPF {} already exists", userDto.getCpf());
            throw new IllegalArgumentException("There is already an User with this CPF.");
        }
       if (userDto.getKeycloakId() == null || userDto.getKeycloakId().isBlank()) {
           log.error("Keycloak ID was not found.");
            throw new IllegalArgumentException("Keycloak ID (sub) is required to create a user.");
        }

        var userEntity = userMapper.toUser(userDto);
        var userSaved = userRepository.save(userEntity);
        log.info("Successfully saved new user.");
        return userMapper.toUserDto(userSaved);
    }

    public UserDto update(Long id, UserDto userDto) {
        var userToUpdate = userRepository.findById(id).orElse(null);

        if (Objects.nonNull(userDto.getCpf()) && !userDto.getCpf().equals(userToUpdate.getCpf())) {
            var isCpfExistingInAnotherUser = userRepository.existsByCpf(userDto.getCpf());
            if (isCpfExistingInAnotherUser) {
                log.error("Cpf {} already in use by another user", userDto.getCpf());
                throw new IllegalArgumentException("This CPF is already in use by another user.");
            }
        }
        userMapper.updateUserFromDto(userDto, userToUpdate);
        var updatedUser = userRepository.save(userToUpdate);
        return userMapper.toUserDto(updatedUser);
    }

    public void delete(Long id) {
        var user = userRepository.findById(id);
        userRepository.deleteById(id);
        log.info("Successfully deleted user with id {}", id);
    }

    @Transactional
    public UserDto ensureAndGetByKeycloakIdentity(String keycloakId, String username, String email, boolean emailVerified) {
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseGet(() -> {
                    User u = new User();
                    u.setKeycloakId(keycloakId);
                    u.setUsername(username);
                    u.setEmail(email);
                    u.setEmailVerified(emailVerified);
                    u.setEnabled(true);
                    return userRepository.save(u);
                });

        boolean changed = false;
        if (Objects.nonNull(username) && !username.equals(user.getUsername())) { user.setUsername(username); changed = true; }
        if (Objects.nonNull(email) && !email.equals(user.getEmail())) { user.setEmail(email); changed = true; }
        if (Objects.isNull(user.getEmailVerified()) || !user.getEmailVerified().equals(emailVerified)) { user.setEmailVerified(emailVerified); changed = true; }
        if (changed) { user = userRepository.save(user); }

        return userMapper.toUserDto(user);
    }
}
