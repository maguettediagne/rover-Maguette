package com.maguette.rover;

/**
 * Erreur prévue (fichier, format ou règle du kata) dont le message est affiché tel quel à l'utilisateur.
 */
public class RoverException extends RuntimeException {

    public RoverException(String message) {
        super(message);
    }

    public RoverException(String message, Throwable cause) {
        super(message, cause);
    }
}
