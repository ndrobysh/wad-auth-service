package com.wad.auth.controller;

import com.wad.auth.dto.LoginRequest;
import com.wad.auth.dto.LoginResponse;
import com.wad.auth.dto.ValidateRequest;
import com.wad.auth.dto.ValidateResponse;
import com.wad.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentification", description = "Endpoints de gestion des tokens")
public class AuthController {

    private final AuthService authService;

    /**
     * POST /api/auth/login
     * Authentifie un utilisateur et retourne un token.
     */
    @Operation(summary = "Connexion", description = "Authentifie un utilisateur et retourne un token valide 1 heure")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Connexion réussie, token retourné"),
            @ApiResponse(responseCode = "401", description = "Identifiants invalides")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("POST /api/auth/login - utilisateur: {}", request.getUsername());
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Validation", description = "Valide un token et renouvelle son expiration si valide")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Token valide, username retourné"),
            @ApiResponse(responseCode = "401", description = "Token invalide ou expiré")
    })
    @PostMapping("/validate")
    public ResponseEntity<ValidateResponse> validate(@Valid @RequestBody ValidateRequest request) {
        log.debug("POST /api/auth/validate");
        ValidateResponse response = authService.validate(request.getToken());

        if (!response.isValid()) {
            return ResponseEntity.status(401).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Déconnexion", description = "Invalide le token de l'utilisateur")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Déconnexion réussie")
    })
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody ValidateRequest request) {
        log.info("POST /api/auth/logout");
        authService.logout(request.getToken());
        return ResponseEntity.ok().build();
    }
}
