package com.wad.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour la requête de connexion.
 * Contient les identifiants de l'utilisateur.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    /**
     * Nom d'utilisateur
     */
    @NotBlank(message = "Le nom d'utilisateur est obligatoire")
    private String username;

    /**
     * Mot de passe
     */
    @NotBlank(message = "Le mot de passe est obligatoire")
    private String password;
}
