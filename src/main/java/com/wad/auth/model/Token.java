package com.wad.auth.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;

/**
 * Modèle représentant un token d'authentification.
 * Format du token : username-date(YYYY/MM/DD)-heure(HH:mm:ss) (chiffré)
 * Durée de validité : 1 heure, renouvelée à chaque appel valide.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "tokens")
public class Token {

    @Id
    private String id;

    /**
     * Token chiffré (format: username-YYYY/MM/DD-HH:mm:ss)
     */
    @Indexed(unique = true)
    private String token;

    /**
     * Nom d'utilisateur associé au token
     */
    @Indexed
    private String username;

    /**
     * Date et heure de création du token
     */
    private LocalDateTime createdAt;

    /**
     * Date et heure d'expiration du token (1 heure après création/renouvellement)
     */
    private LocalDateTime expiresAt;
}
