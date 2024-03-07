package com.home.financial.app.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole(T(com.home.financial.app.domain.Role).ADMIN)")
@Hidden
public class AdminController {
    @GetMapping
    @PreAuthorize("hasAuthority(T(com.home.financial.app.domain.Permission).ADMIN_READ)")
    public String get() {
        return "GET:: admin controller";
    }
    @PostMapping
    @PreAuthorize("hasAuthority(T(com.home.financial.app.domain.Permission).ADMIN_CREATE)")
    public String post() {
        return "POST:: admin controller";
    }
    @PutMapping
    @PreAuthorize("hasAuthority(T(com.home.financial.app.domain.Permission).ADMIN_UPDATE)")
    public String put() {
        return "PUT:: admin controller";
    }
    @DeleteMapping
    @PreAuthorize("hasAuthority(T(com.home.financial.app.domain.Permission).ADMIN_DELETE)")
    public String delete() {
        return "DELETE:: admin controller";
    }
}
