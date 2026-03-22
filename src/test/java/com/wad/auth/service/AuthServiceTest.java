package com.wad.auth.service;

import com.wad.auth.config.TokenEncryptionConfig;
import com.wad.auth.dto.LoginRequest;
import com.wad.auth.dto.LoginResponse;
import com.wad.auth.model.User;
import com.wad.auth.repository.TokenRepository;
import com.wad.auth.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AuthServiceTest {

    // tests rapides avant rendu, faudrait en ajouter plus

    @Test
    void testLoginOk() {
        var userRepo = Mockito.mock(UserRepository.class);
        var tokenRepo = Mockito.mock(TokenRepository.class);
        var encoder = new BCryptPasswordEncoder();
        var encryption = Mockito.mock(TokenEncryptionConfig.class);

        var user = new User();
        user.setUsername("player1");
        user.setPassword(encoder.encode("password123"));

        when(userRepo.findByUsername("player1")).thenReturn(Optional.of(user));
        when(encryption.encrypt(any())).thenReturn("token-chiffre");

        var service = new AuthService(userRepo, tokenRepo, encryption, encoder);
        LoginResponse resp = service.login(new LoginRequest("player1", "password123"));
        assertNotNull(resp);
        assertEquals("token-chiffre", resp.getToken());
    }

    @Test
    void testLoginMauvaisMdp() {
        var userRepo = Mockito.mock(UserRepository.class);
        var tokenRepo = Mockito.mock(TokenRepository.class);
        var encoder = new BCryptPasswordEncoder();
        var encryption = Mockito.mock(TokenEncryptionConfig.class);

        var user = new User();
        user.setUsername("player1");
        user.setPassword(encoder.encode("password123"));

        when(userRepo.findByUsername("player1")).thenReturn(Optional.of(user));

        var service = new AuthService(userRepo, tokenRepo, encryption, encoder);
        assertThrows(AuthService.AuthenticationException.class,
                () -> service.login(new LoginRequest("player1", "mauvais")));
    }
}
