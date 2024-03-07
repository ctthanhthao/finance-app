package com.home.financial.app.dto;

import com.home.financial.app.domain.Role;

public record UserRegisterRequest (String firstname, String lastname, String email, String username, String password) {
    public UserCreatedRequest toCreatedUser() {
        return UserCreatedRequest
                .builder()
                .email(email)
                .firstname(firstname)
                .lastname(lastname)
                .username(username)
                .password(password)
                .role(Role.USER)
                .build();
    }
}
