package org.Chess.Domain;

import org.Chess.Domain.Model.ChessBoard;
import org.Chess.Domain.Model.PieceColor;
import org.Chess.Domain.Model.PieceType;
import org.Chess.Domain.Model.Square;

public class BoardPrinter {

    public static void print(ChessBoard board) {
        for (int row = 7; row >= 0; row--) {          // rank 8 on top, rank 1 at the bottom
            StringBuilder line = new StringBuilder();
            line.append(row + 1).append("  ");

            for (int col = 0; col < 8; col++) {
                Square square = new Square(row, col);
                line.append(symbol(board, square)).append(" ");
            }
            System.out.println(line);
        }
        System.out.println();
        System.out.println("   a  b  c  d  e  f  g  h");
    }

    private static String symbol(ChessBoard board, Square square) {
        return board.pieceAt(square)
                .map(p -> (p.color() == PieceColor.WHITE ? "W" : "B") + letter(p.type()))
                .orElse("--");
    }

    private static char letter(PieceType type) {
        return switch (type) {
            case PAWN   -> 'P';
            case KNIGHT -> 'N';   // N, because K is taken by the king
            case BISHOP -> 'B';
            case ROOK   -> 'R';
            case QUEEN  -> 'Q';
            case KING   -> 'K';
        };
    }
}