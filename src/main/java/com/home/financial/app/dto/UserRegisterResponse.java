package com.home.financial.app.dto;

import com.home.financial.app.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterResponse {
    private String email;
    private String username;
    @Builder.Default
    private Role role = Role.USER;
}
