package com.wad.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour la réponse de connexion.
 * Retourne le token généré en cas de succès.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    /**
     * Token d'authentification chiffré
     */
    private String token;

    /**
     * Message de statut (optionnel)
     */
    private String message;

    /**
     * Constructeur avec token uniquement
     */
    public LoginResponse(String token) {
        this.token = token;
        this.message = "Connexion réussie";
    }
}
