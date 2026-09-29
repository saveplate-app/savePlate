package com.saveplate.api.exceptions;

public class InvalidResetTokenException extends RuntimeException {
    public InvalidResetTokenException() {
        super("Token de réinitialisation invalide ou expiré");
    }
}