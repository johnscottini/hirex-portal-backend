package com.hirex.users.mapper;

import com.hirex.users.domain.User;
import com.hirex.users.dto.UserDto;
import com.hirex.users.dto.UserResumoDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResumoDto toUserResumoDto(User user);
    List<UserResumoDto> toUsersResumoDto(Iterable<User> user);

    UserDto toUserDto(User user);

    User toUser(UserDto dto);

    void updateUserFromDto(UserDto userDto, @MappingTarget User user);
}
