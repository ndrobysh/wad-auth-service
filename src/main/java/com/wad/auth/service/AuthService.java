package com.wad.auth.service;

import com.wad.auth.config.TokenEncryptionConfig;
import com.wad.auth.dto.LoginRequest;
import com.wad.auth.dto.LoginResponse;
import com.wad.auth.dto.ValidateResponse;
import com.wad.auth.model.Token;
import com.wad.auth.model.User;
import com.wad.auth.repository.TokenRepository;
import com.wad.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final TokenEncryptionConfig encryptionConfig;
    private final PasswordEncoder passwordEncoder;

    private static final int TOKEN_VALIDITY_HOURS = 1;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    public LoginResponse login(LoginRequest request) {
        log.info("Tentative de connexion pour l'utilisateur: {}", request.getUsername());

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> {
                    log.warn("Utilisateur non trouvé: {}", request.getUsername());
                    return new AuthenticationException("Identifiants invalides");
                });

        // Vérification du mot de passe avec BCrypt
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("Mot de passe incorrect pour l'utilisateur: {}", request.getUsername());
            throw new AuthenticationException("Identifiants invalides");
        }

        tokenRepository.deleteByUsername(user.getUsername());

        String tokenValue = generateToken(user.getUsername());
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiresAt = now.plusHours(TOKEN_VALIDITY_HOURS);

        Token token = new Token();
        token.setToken(tokenValue);
        token.setUsername(user.getUsername());
        token.setCreatedAt(now);
        token.setExpiresAt(expiresAt);

        tokenRepository.save(token);

        log.info("Connexion réussie pour l'utilisateur: {}", user.getUsername());
        return new LoginResponse(tokenValue);
    }

    /**
     * Valide un token et retourne le nom d'utilisateur associé.
     * Renouvelle automatiquement la date d'expiration si le token est valide.
     *
     * @param tokenValue le token à valider
     * @return les informations de validation
     */
    public ValidateResponse validate(String tokenValue) {
        log.debug("Validation du token");

        Optional<Token> tokenOpt = tokenRepository.findByToken(tokenValue);

        if (tokenOpt.isEmpty()) {
            log.warn("Token non trouvé");
            return ValidateResponse.invalid("Token invalide");
        }

        Token token = tokenOpt.get();

        // Vérification de l'expiration
        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            log.warn("Token expiré pour l'utilisateur: {}", token.getUsername());
            tokenRepository.delete(token);
            return ValidateResponse.invalid("Token expiré");
        }

        // Renouvellement de la date d'expiration
        token.setExpiresAt(LocalDateTime.now().plusHours(TOKEN_VALIDITY_HOURS));
        tokenRepository.save(token);

        log.debug("Token valide pour l'utilisateur: {}", token.getUsername());
        return ValidateResponse.valid(token.getUsername());
    }

    /**
     * Déconnecte un utilisateur en supprimant son token.
     *
     * @param tokenValue le token à invalider
     */
    public void logout(String tokenValue) {
        log.info("Déconnexion demandée");
        tokenRepository.deleteByToken(tokenValue);
        log.info("Déconnexion effectuée");
    }

    /**
     * Génère un token au format : username-YYYY/MM/DD-HH:mm:ss (chiffré avec AES)
     *
     * @param username le nom d'utilisateur
     * @return le token chiffré
     */
    private String generateToken(String username) {
        LocalDateTime now = LocalDateTime.now();
        String rawToken = String.format("%s-%s-%s",
                username,
                now.format(DATE_FORMATTER),
                now.format(TIME_FORMATTER));

        // Chiffrement AES du token
        return encryptionConfig.encrypt(rawToken);
    }

    /**
     * Déchiffre un token pour extraire les informations
     *
     * @param encryptedToken le token chiffré
     * @return le contenu déchiffré (username-date-heure)
     */
    public String decryptToken(String encryptedToken) {
        return encryptionConfig.decrypt(encryptedToken);
    }

    /**
     * Exception personnalisée pour les erreurs d'authentification
     */
    public static class AuthenticationException extends RuntimeException {
        public AuthenticationException(String message) {
            super(message);
        }
    }
}
