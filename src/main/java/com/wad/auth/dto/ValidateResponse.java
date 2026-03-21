package com.wad.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidateResponse {

    private String username;

    private boolean valid;

    private String message;

    public static ValidateResponse valid(String username) {
        return new ValidateResponse(username, true, "Token valide");
    }

    public static ValidateResponse invalid(String message) {
        return new ValidateResponse(null, false, message);
    }
}
