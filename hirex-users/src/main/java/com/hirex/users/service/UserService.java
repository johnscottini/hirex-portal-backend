package com.hirex.users.service;

import com.google.common.base.Strings;
import com.hirex.config.exception.exceptions.BusinessException;
import com.hirex.users.dto.UserDto;
import com.hirex.users.dto.UserResumoDto;
import com.hirex.users.mapper.UserMapper;
import com.hirex.users.repository.UserRepository;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {

    protected PageRequest getPage(Integer pageIndex, Integer pageSizer, String sortField){
        return PageRequest.of(pageIndex, pageSizer, getSortExp(sortField));
    }
    protected Sort getSortExp(String sortField) {
        if(Strings.isNullOrEmpty(sortField)){
            return Sort.unsorted();
        }

        var direction = Sort.Direction.ASC;
        return Sort.by(direction, sortField);
    }


    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public List<UserResumoDto> findAll(Integer pageIndex, Integer pageSize, String sortField) {


        var users = userRepository.findAll(null, getPage(pageIndex, pageSize, "name"));

        return userMapper.toUsersResumoDto(users);
    }

    public UserDto get(Long id) {
        var users = userRepository.require(id);
        return userMapper.toUserDto(users);

    }

    public UserDto save(UserDto userDto) {
        var isCpfExisting = userRepository.existsByCpf(userDto.getCpf());
        if(isCpfExisting) {
            throw new BusinessException("There is already an User with this CPF.");
        }

        var userEntity = userMapper.toUser(userDto);
        var userSaved = userRepository.save(userEntity);
        return userMapper.toUserDto(userSaved);
    }

    public UserDto update(Long id, UserDto userDto) {
        var userToUpdate = userRepository.require(id);

        if (Objects.nonNull(userDto.getCpf()) && !userDto.getCpf().equals(userToUpdate.getCpf())) {
            var bb = new BooleanBuilder();
            //bb.and(QUsers.u.cpf.eq(userDto.getCpf()));
            //bb.and(user.id.ne(id));

            var isCpfExistingInAnotherUser = userRepository.exists(bb);
            if (isCpfExistingInAnotherUser) {
                throw new BusinessException("This CPF is already in use by another user.");
            }
        }
        userMapper.updateUserFromDto(userDto, userToUpdate);
        var updatedUser = userRepository.save(userToUpdate);
        return userMapper.toUserDto(updatedUser);
    }

    public void delete(Long id) {
        var user = userRepository.require(id);
        userRepository.delete(user);
    }
}
