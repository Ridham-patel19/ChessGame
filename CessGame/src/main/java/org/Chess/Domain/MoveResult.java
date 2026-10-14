package org.Chess.Domain;

import java.util.Optional;

public record MoveResult(Move move ,
                         Optional<Piece> captueredPiece ,
                         GameStatus status ,
                         PieceColor nextToMove) {


}
