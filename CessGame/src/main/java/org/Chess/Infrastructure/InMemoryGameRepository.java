package org.Chess.Infrastructure;

import org.Chess.Application.GameNotFoundException;
import org.Chess.Application.GameRepository;
import org.Chess.Domain.Game.Game;
import org.Chess.Domain.Game.GameId;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryGameRepository implements GameRepository {
    private final Map<GameId , Game> games = new HashMap<>();

    @Override
    public Game save(Game game) {
       games.put(game.id() , game);

       return game;
    }

    @Override
    public Optional<Game> findById(GameId id) {
        return Optional.ofNullable(games.get(id));
    }

    @Override
    public void delete(GameId id) {
        games.remove(id);
    }
}
