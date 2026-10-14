package org.Chess.Domain;

public class NotYourTureException extends RuntimeException {
    public NotYourTureException(String message) {
        super(message);
    }
}
