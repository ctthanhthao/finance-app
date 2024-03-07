package com.home.financial.app.dto;

import com.home.financial.app.domain.Role;
import lombok.Builder;

@Builder
public record UserDto(String username, String firstname, String lastname, String email, Role role) {
}
