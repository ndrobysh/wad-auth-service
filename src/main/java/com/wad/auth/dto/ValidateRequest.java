package com.wad.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour la requête de validation de token.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidateRequest {

    /**
     * Token à valider
     */
    @NotBlank(message = "Le token est obligatoire")
    private String token;
}
