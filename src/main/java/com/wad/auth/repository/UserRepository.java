package com.wad.auth.repository;

import com.wad.auth.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository pour les opérations sur les utilisateurs.
 * Interface Spring Data MongoDB pour la collection "users".
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {

    /**
     * Recherche un utilisateur par son nom d'utilisateur
     * @param username le nom d'utilisateur
     * @return l'utilisateur trouvé (optionnel)
     */
    Optional<User> findByUsername(String username);

    /**
     * Vérifie si un utilisateur existe avec ce nom
     * @param username le nom d'utilisateur
     * @return true si l'utilisateur existe
     */
    boolean existsByUsername(String username);
}
