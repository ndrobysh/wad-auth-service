package com.wad.auth.controller;

import com.wad.auth.dto.LoginRequest;
import com.wad.auth.dto.LoginResponse;
import com.wad.auth.dto.ValidateRequest;
import com.wad.auth.dto.ValidateResponse;
import com.wad.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST pour l'authentification.
 * Expose les endpoints de connexion, validation et déconnexion.
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * POST /api/auth/login
     * Authentifie un utilisateur et retourne un token.
     *
     * @param request les identifiants de connexion (username, password)
     * @return 200 avec le token si succès, 401 si échec
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("POST /api/auth/login - utilisateur: {}", request.getUsername());
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/auth/validate
     * Valide un token et retourne le nom d'utilisateur associé.
     * Renouvelle automatiquement la date d'expiration si valide.
     *
     * @param request le token à valider
     * @return 200 avec username si valide, 401 si invalide/expiré
     */
    @PostMapping("/validate")
    public ResponseEntity<ValidateResponse> validate(@Valid @RequestBody ValidateRequest request) {
        log.debug("POST /api/auth/validate");
        ValidateResponse response = authService.validate(request.getToken());

        if (!response.isValid()) {
            return ResponseEntity.status(401).body(response);
        }
        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/auth/logout
     * Déconnecte un utilisateur en invalidant son token.
     *
     * @param request le token à invalider
     * @return 200 si succès
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody ValidateRequest request) {
        log.info("POST /api/auth/logout");
        authService.logout(request.getToken());
        return ResponseEntity.ok().build();
    }
}
