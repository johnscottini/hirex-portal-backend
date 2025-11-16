package com.hirex.users.service;

import com.hirex.common.pagination.PageUtils;
import com.hirex.users.UserMapper;
import com.hirex.users.UserRepository;
import com.hirex.users.dto.UserDto;
import com.hirex.users.dto.UserResumoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public List<UserResumoDto> findAll(Integer pageIndex, Integer pageSize, String sortField) {

        var users = userRepository.findAll(PageUtils.page(pageIndex, pageSize, "username"));

        return userMapper.toUsersResumoDto(users);
    }

    public UserDto get(Long id) {
        var users = userRepository.findById(id).orElse(null);
        return userMapper.toUserDto(users);

    }

    public UserDto save(UserDto userDto) {
        var isCpfExisting = userRepository.existsByCpf(userDto.getCpf());
        if(isCpfExisting) {
            throw new IllegalArgumentException("There is already an User with this CPF.");
        }

        var userEntity = userMapper.toUser(userDto);
        var userSaved = userRepository.save(userEntity);
        return userMapper.toUserDto(userSaved);
    }

    public UserDto update(Long id, UserDto userDto) {
        var userToUpdate = userRepository.findById(id).orElse(null);

        if (Objects.nonNull(userDto.getCpf()) && !userDto.getCpf().equals(userToUpdate.getCpf())) {
            var isCpfExistingInAnotherUser = userRepository.existsByCpf(userDto.getCpf());
            if (isCpfExistingInAnotherUser) {
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
    }
}
