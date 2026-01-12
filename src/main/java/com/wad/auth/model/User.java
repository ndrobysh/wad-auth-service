package com.wad.auth.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

/**
 * Modèle représentant un utilisateur dans le système d'authentification.
 * Stocké dans la collection "users" de MongoDB.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User {

    @Id
    private String id;

    /**
     * Nom d'utilisateur unique
     */
    @Indexed(unique = true)
    private String username;

    /**
     * Mot de passe (hashé)
     */
    private String password;
}
