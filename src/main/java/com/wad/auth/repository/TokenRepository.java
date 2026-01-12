package com.wad.auth.repository;

import com.wad.auth.model.Token;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository pour les opérations sur les tokens.
 * Interface Spring Data MongoDB pour la collection "tokens".
 */
@Repository
public interface TokenRepository extends MongoRepository<Token, String> {

    /**
     * Recherche un token par sa valeur chiffrée
     * @param token la valeur du token
     * @return le token trouvé (optionnel)
     */
    Optional<Token> findByToken(String token);

    /**
     * Recherche tous les tokens d'un utilisateur
     * @param username le nom d'utilisateur
     * @return liste des tokens de l'utilisateur
     */
    List<Token> findByUsername(String username);

    /**
     * Supprime tous les tokens d'un utilisateur (logout)
     * @param username le nom d'utilisateur
     */
    void deleteByUsername(String username);

    /**
     * Supprime un token par sa valeur
     * @param token la valeur du token
     */
    void deleteByToken(String token);

    /**
     * Supprime tous les tokens expirés
     * @param dateTime la date/heure de référence
     */
    void deleteByExpiresAtBefore(LocalDateTime dateTime);
}
