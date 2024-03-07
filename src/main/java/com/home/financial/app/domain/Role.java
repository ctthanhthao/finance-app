package com.home.financial.app.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.home.financial.app.domain.Permission.ADMIN_CREATE;
import static com.home.financial.app.domain.Permission.ADMIN_DELETE;
import static com.home.financial.app.domain.Permission.ADMIN_READ;
import static com.home.financial.app.domain.Permission.ADMIN_UPDATE;
import static com.home.financial.app.domain.Permission.MANAGER_CREATE;
import static com.home.financial.app.domain.Permission.MANAGER_DELETE;
import static com.home.financial.app.domain.Permission.MANAGER_READ;
import static com.home.financial.app.domain.Permission.MANAGER_UPDATE;

@RequiredArgsConstructor
public enum Role {
    USER(Collections.emptySet()),
    ADMIN(
            Set.of(
                    ADMIN_READ,
                    ADMIN_UPDATE,
                    ADMIN_DELETE,
                    ADMIN_CREATE,
                    MANAGER_READ,
                    MANAGER_UPDATE,
                    MANAGER_DELETE,
                    MANAGER_CREATE
            )
    ),
    MANAGER(
            Set.of(
                    MANAGER_READ,
                    MANAGER_UPDATE,
                    MANAGER_DELETE,
                    MANAGER_CREATE
            )
    );

    @Getter
    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(p -> new SimpleGrantedAuthority(p.name()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
