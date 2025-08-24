package com.hirex.portal.mapper;

import com.hirex.portal.domain.User;
import dto.UserDto;
import dto.UserResumoDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResumoDto toUserResumoDto(User user);
    List<UserResumoDto> toUsersResumoDto(Iterable<User> user);

    UserDto toUserDto(User user);
}
