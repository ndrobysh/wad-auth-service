package com.wad.auth.repository;

import com.wad.auth.model.Token;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends MongoRepository<Token, String> {

    Optional<Token> findByToken(String token);

    List<Token> findByUsername(String username);

    void deleteByUsername(String username);

    void deleteByToken(String token);

    void deleteByExpiresAtBefore(LocalDateTime dateTime);
}
