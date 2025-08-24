package com.hirex.portal.controller;

import com.hirex.portal.service.UserService;
import dto.UserDto;
import dto.UserResumoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user")
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserResumoDto> findAll() {
        return userService.findAll();
    }

    @GetMapping("{id}")
    public UserDto findById(@PathVariable Long id) {
        return userService.get(id);
    }
}
