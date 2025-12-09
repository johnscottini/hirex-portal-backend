package com.hirex.jobapplication.client;

import com.hirex.jobapplication.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "users-service")
public interface UserClient {

    @GetMapping("/api/user/{id}")
    UserResponse getUserById(@PathVariable Long id);
}