package org.Chess.ConsoleGame;

import org.Chess.Domain.Model.PieceType;
import org.Chess.Domain.Model.Square;

public record ParsedMoves(Square from , Square to , PieceType promotion) {
}


