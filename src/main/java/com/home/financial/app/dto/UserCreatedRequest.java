package com.home.financial.app.dto;

import com.home.financial.app.domain.Role;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class UserCreatedRequest {
    private String firstname;
    private String lastname;
    private String email;
    private String username;
    private String password;
    private final Role role;
}
