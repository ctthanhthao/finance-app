package com.home.financial.app.repositories;

import com.home.financial.app.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {
    Optional<Token> findByToken(String token);

    @Query(value = """
        SELECT t FROM Token t \s 
        INNER JOIN User u ON t.user.id = u.id \s 
        WHERE u.id = :userId
        """)
    Optional<Token> findByUserId(Integer userId);
}
