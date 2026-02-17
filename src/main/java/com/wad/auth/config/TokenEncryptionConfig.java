package com.wad.auth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Configuration et utilitaires pour le chiffrement des tokens.
 * Utilise AES pour chiffrer le format : username-YYYY/MM/DD-HH:mm:ss
 */
@Configuration
public class TokenEncryptionConfig {

    /**
     * Clé secrète pour le chiffrement AES (16 caractères = 128 bits)
     */
    @Value("${auth.token.secret-key:WadGachaSecret16}")
    private String secretKey;

    private static final String ALGORITHM = "AES";

    /**
     * Chiffre une chaîne avec AES
     * @param data la chaîne à chiffrer
     * @return la chaîne chiffrée en Base64
     */
    public String encrypt(String data) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(
                    padKey(secretKey).getBytes(StandardCharsets.UTF_8), 
                    ALGORITHM
            );
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] encrypted = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors du chiffrement du token", e);
        }
    }

    /**
     * Déchiffre une chaîne chiffrée avec AES
     * @param encryptedData la chaîne chiffrée en Base64
     * @return la chaîne déchiffrée
     */
    public String decrypt(String encryptedData) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(
                    padKey(secretKey).getBytes(StandardCharsets.UTF_8), 
                    ALGORITHM
            );
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decoded = Base64.getUrlDecoder().decode(encryptedData);
            byte[] decrypted = cipher.doFinal(decoded);
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors du déchiffrement du token", e);
        }
    }

    /**
     * Assure que la clé fait exactement 16 caractères (128 bits)
     */
    private String padKey(String key) {
        if (key.length() >= 16) {
            return key.substring(0, 16);
        }
        return String.format("%-16s", key).replace(' ', '0');
    }
}
