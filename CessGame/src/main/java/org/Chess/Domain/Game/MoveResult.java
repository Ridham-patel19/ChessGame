package org.Chess.Domain.Game;

import org.Chess.Domain.Model.Piece;
import org.Chess.Domain.Model.PieceColor;
import org.Chess.Domain.Moves.Move;

import java.util.Optional;

public record MoveResult(Move move ,
                         Optional<Piece> captueredPiece ,
                         GameStatus status ,
                         PieceColor nextToMove) {


}
