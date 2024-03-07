package com.home.financial.app.service;

import com.home.financial.app.common.util.UserUtils;
import com.home.financial.app.domain.User;
import com.home.financial.app.dto.ChangePasswordRequest;
import com.home.financial.app.dto.UserDto;
import com.home.financial.app.dto.UserCreatedRequest;
import com.home.financial.app.dto.UserUpdateRequest;
import com.home.financial.app.exception.*;
import com.home.financial.app.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Page<UserDto> getAll(int pageNum, int pageSize) {
        return userRepository.findAll(PageRequest.of(pageNum, pageSize)).map(User::toUserDto);
    }

    public UserDto getUser(String username) {
        return User.toUserDto(findUserByUsername(username));
    }

    public UserDto create(UserCreatedRequest req) {
        validateNewUser(req);
        var user = User
                .builder()
                .username(req.getUsername())
                .firstname(req.getFirstname())
                .lastname(req.getLastname())
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .role(req.getRole())
                .enabled(Boolean.TRUE)
                .build();
        userRepository.save(user);
        return User.toUserDto(user);
    }

    private void validateNewUser(UserCreatedRequest req) {
        boolean usernameExists = userRepository.existsByUsername(req.getUsername());
        if (usernameExists) {
            throw new UsernameExistsException();
        }
        boolean emailExists = userRepository
                .findByEmail(req.getEmail())
                .orElse(null) == null ? Boolean.FALSE : Boolean.TRUE;
        if (emailExists) {
            throw new EmailExistsException();
        }

        if (!UserUtils.isValidEmail(req.getEmail())) {
            throw new EmailValidationException();
        }

        if (!UserUtils.isValidUserName(req.getUsername())) {
            throw new UsernameValidationException();
        }

    }

    public void updateUser(String username, UserUpdateRequest request) {
        var user = findUserByUsername(username);
        if (Objects.nonNull(request.getFirstname())) {
            user.setFirstname(request.getFirstname());
        }
        if (Objects.nonNull(request.getLastname())) {
            user.setLastname(request.getLastname());
        }
        if (Objects.nonNull(request.getEnabled())) {
            user.setEnabled(request.getEnabled());
        }
        userRepository.save(user);
    }

    public void deleteUser(String username) {
        var user = findUserByUsername(username);
        userRepository.delete(user);
    }

    public void changePassword(ChangePasswordRequest req) {
        var user = getCurrentUser();
        var encodedOldPassword = passwordEncoder.encode(req.getOldPassword());
        if(encodedOldPassword.equals(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(req.getNewPassword()));
            userRepository.save(user);
        }
        throw new PasswordNotMatchException();
    }
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(UsernameNotFoundException::new);
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() != null) {
            var user = (User) authentication.getPrincipal();
            return findUserByUsername(user.getUsername());
        }
        return null;
    }
}
