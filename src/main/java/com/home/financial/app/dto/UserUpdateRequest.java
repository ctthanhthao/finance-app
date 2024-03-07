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
public class UserUpdateRequest {
    private String firstname;
    private String lastname;
    private Boolean enabled;
}
