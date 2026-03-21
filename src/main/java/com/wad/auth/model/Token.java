package com.wad.auth.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;

// format token chiffre : username-YYYY/MM/DD-HH:mm:ss
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "tokens")
public class Token {

    @Id
    private String id;

    @Indexed(unique = true)
    private String token;

    @Indexed
    private String username;

    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;
}
