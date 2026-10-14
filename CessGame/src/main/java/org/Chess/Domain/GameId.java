package org.Chess.Domain;

import java.util.UUID;

public record GameId(UUID value) {
 public static GameId newId(){return new GameId(UUID.randomUUID());};
}
