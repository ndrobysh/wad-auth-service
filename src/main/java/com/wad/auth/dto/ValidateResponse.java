package com.wad.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour la réponse de validation de token.
 * Retourne le nom d'utilisateur si le token est valide.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidateResponse {

    /**
     * Nom d'utilisateur associé au token
     */
    private String username;

    /**
     * Indique si le token est valide
     */
    private boolean valid;

    /**
     * Message de statut
     */
    private String message;

    /**
     * Constructeur pour token valide
     */
    public static ValidateResponse valid(String username) {
        return new ValidateResponse(username, true, "Token valide");
    }

    /**
     * Constructeur pour token invalide
     */
    public static ValidateResponse invalid(String message) {
        return new ValidateResponse(null, false, message);
    }
}
