package org.Chess.Application;

import org.Chess.Domain.Game.Game;
import org.Chess.Domain.Game.GameId;

import java.util.Optional;

public interface GameRepository {
    Game save(Game game);

    Optional<Game> findById(GameId id);
    void delete(GameId id);
}
