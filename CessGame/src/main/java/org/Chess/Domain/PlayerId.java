package org.Chess.Domain;

public record PlayerId(String value) {

    public PlayerId{
        if(value == null || value.isBlank()) throw new IllegalArgumentException("PlayerId needs a value");
    }
}
