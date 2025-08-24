package com.hirex.portal.service;

import com.hirex.portal.mapper.UserMapper;
import com.hirex.portal.repository.UserRepository;
import dto.UserDto;
import dto.UserResumoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public List<UserResumoDto> findAll() {

        var users = userRepository.findAll();

        return userMapper.toUsersResumoDto(users);
    }

    public UserDto get(Long id) {
        var users = userRepository.require(id);
        return userMapper.toUserDto(users);
    }
}
