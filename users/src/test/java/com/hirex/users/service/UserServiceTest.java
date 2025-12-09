package com.hirex.users.service;

import com.hirex.users.UserMapper;
import com.hirex.users.UserRepository;
import com.hirex.users.domain.User;
import com.hirex.users.domain.enums.Gender;
import com.hirex.users.dto.UserDto;
import com.hirex.users.dto.UserResumoDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User userEntity;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        userEntity = new User();
        userEntity.setId(1L);
        userEntity.setUsername("john");
        userEntity.setCpf("12345678900");
        userEntity.setFullName("John Doe");
        userEntity.setGender(Gender.M);
        userEntity.setBirthDate(LocalDate.of(1990, 1, 1));
        userEntity.setEmail("john@ex.com");

        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setUsername("john");
        userDto.setCpf("12345678900");
        userDto.setFullName("John Doe");
        userDto.setGender(Gender.M);
        userDto.setBirthDate(LocalDate.of(1990, 1, 1));
        userDto.setEmail("john@ex.com");
        userDto.setKeycloakId("kc-123");
    }

    @Test
    @DisplayName("findAll returns mapped resumo DTOs with paging by username")
    void findAll_shouldReturnResumoDtos() {
        Page<User> page = new PageImpl<>(List.of(userEntity));
        when(userRepository.findAll(any(PageRequest.class))).thenReturn(page);
        UserResumoDto resumo = new UserResumoDto();
        resumo.setId(1L);
        resumo.setUsername("john");
        resumo.setFullName("John Doe");
        resumo.setEmail("john@ex.com");
        resumo.setCpf("12345678900");
        when(userMapper.toUsersResumoDto(page)).thenReturn(List.of(resumo));

        List<UserResumoDto> result = userService.findAll(0, 10, "username");

        assertEquals(1, result.size());
        assertEquals("john", result.get(0).getUsername());

        ArgumentCaptor<PageRequest> captor = ArgumentCaptor.forClass(PageRequest.class);
        verify(userRepository).findAll(captor.capture());
        PageRequest pr = captor.getValue();
        assertEquals(0, pr.getPageNumber());
        assertEquals(10, pr.getPageSize());
    }

    @Test
    @DisplayName("get maps entity to DTO when found")
    void get_shouldMapEntityToDto() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        when(userMapper.toUserDto(userEntity)).thenReturn(userDto);

        UserDto result = userService.get(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    @DisplayName("save persists a new user when CPF is not taken")
    void save_shouldPersistWhenCpfNotExists() {
        when(userRepository.existsByCpf("12345678900")).thenReturn(false);
        when(userMapper.toUser(userDto)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(userMapper.toUserDto(userEntity)).thenReturn(userDto);

        UserDto saved = userService.save(userDto);

        assertNotNull(saved);
        verify(userRepository).save(userEntity);
    }

    @Test
    @DisplayName("save throws when CPF already exists")
    void save_shouldThrowWhenCpfExists() {
        when(userRepository.existsByCpf("12345678900")).thenReturn(true);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> userService.save(userDto));
        assertTrue(ex.getMessage().toLowerCase().contains("cpf"));
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("update throws when changing CPF to an existing one in another user")
    void update_shouldThrowWhenCpfConflicts() {
        User existing = new User();
        existing.setId(1L);
        existing.setCpf("11111111111");
        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));

        UserDto patch = new UserDto();
        patch.setCpf("22222222222");
        when(userRepository.existsByCpf("22222222222")).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> userService.update(1L, patch));
        assertTrue(ex.getMessage().toLowerCase().contains("cpf"));
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("update maps fields and saves when valid")
    void update_shouldMapAndSave() {
        User toUpdate = new User();
        toUpdate.setId(1L);
        toUpdate.setCpf("11111111111");
        when(userRepository.findById(1L)).thenReturn(Optional.of(toUpdate));

        UserDto patch = new UserDto();
        patch.setFullName("New Name");

        doAnswer(invocation -> {
            UserDto src = invocation.getArgument(0);
            User target = invocation.getArgument(1);
            target.setFullName(src.getFullName());
            return null;
        }).when(userMapper).updateUserFromDto(any(UserDto.class), any(User.class));

        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));
        when(userMapper.toUserDto(any(User.class))).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            UserDto dto = new UserDto();
            dto.setId(u.getId());
            dto.setFullName(u.getFullName());
            dto.setCpf(u.getCpf());
            return dto;
        });

        UserDto updated = userService.update(1L, patch);

        assertEquals("New Name", updated.getFullName());
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("delete delegates to repository")
    void delete_shouldCallRepository() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        userService.delete(1L);
        verify(userRepository).deleteById(1L);
    }
}
