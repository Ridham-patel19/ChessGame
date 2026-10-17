package org.Chess.Domain.Moves;

import org.Chess.Domain.Model.*;

import java.util.Optional;

public class AttackDetecter {

    private final PieceMovesStrategy legalMoves = new PieceMovesStrategy();

    public boolean isAttacked(ChessBoard board, Square target, PieceColor byColor) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Square square = new Square(row, col);
                Optional<Piece> piece = board.pieceAt(square);

                if (piece.isEmpty() || piece.get().color() != byColor) continue;

                if (piece.get().type() == PieceType.PAWN) {
                    if (pawnAttacks(square, target, byColor)) return true;
                } else if (legalMoves.movesFrom(board, square).contains(target)) {
                    return true;
                }
            }
        }
        return false;
    }

    // a pawn attacks the two squares diagonally in front of it, whether occupied or not
    private boolean pawnAttacks(Square pawn, Square target, PieceColor color) {
        int direction = color == PieceColor.WHITE ? 1 : -1;
        return target.row() == pawn.row() + direction
                && Math.abs(target.column() - pawn.column()) == 1;
    }
}
