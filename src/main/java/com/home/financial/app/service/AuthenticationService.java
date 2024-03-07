package com.home.financial.app.service;

import com.home.financial.app.domain.Token;
import com.home.financial.app.domain.User;
import com.home.financial.app.dto.*;
import com.home.financial.app.repositories.TokenRepository;
import com.home.financial.app.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public UserRegisterResponse register(UserRegisterRequest req) {
        userService.create(req.toCreatedUser());
        return UserRegisterResponse
                .builder()
                .email(req.email())
                .username(req.username())
                .build();
    }

    public AuthenticationResponse login(AuthenticationRequest req) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        req.getUsername(),
                        req.getPassword()
                )
        );

        var user = userService.findUserByUsername(req.getUsername());
        var tokenDb = tokenRepository.findByUserId(user.getId())
                .orElse(null);
        if (tokenDb == null) {
            tokenDb = new Token();
            tokenDb.setUser(user);
        }
        var jwtToken = jwtService.generateToken(user);
        tokenDb.setToken(jwtToken);
        tokenRepository.save(tokenDb);
        return AuthenticationResponse
                .builder()
                .token(jwtToken)
                .build();
    }

    public void logout() {
        var user = userService.getCurrentUser();
        if (user != null) {
            Token tokenDb = tokenRepository.findByToken(user.getToken().getToken()).orElse(null);
            if (tokenDb != null) {
                tokenRepository.delete(tokenDb);
                SecurityContextHolder.clearContext();
            }
        }
    }
}
