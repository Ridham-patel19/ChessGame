package org.Chess.Application;

import org.Chess.Domain.Game.GameId;

public class GameNotFoundException extends RuntimeException {
    public GameNotFoundException(GameId id) {
        super("This game does not exist : " + id);
    }
}
