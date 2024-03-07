package com.home.financial.app.service;

import com.home.financial.app.domain.Token;
import com.home.financial.app.repositories.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final TokenRepository tokenRepository;

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer "))
            return;
        final String jwtToken = authHeader.substring(7);
        Token tokenDb = tokenRepository.findByToken(jwtToken).orElse(null);
        if (tokenDb != null) {
            tokenRepository.delete(tokenDb);
            SecurityContextHolder.clearContext();
        }
    }
}
