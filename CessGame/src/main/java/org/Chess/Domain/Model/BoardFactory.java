package org.Chess.Domain.Model;

public class BoardFactory {

    private static final PieceType[] BACK_RANK = {
            PieceType.ROOK, PieceType.KNIGHT, PieceType.BISHOP, PieceType.QUEEN,
            PieceType.KING, PieceType.BISHOP, PieceType.KNIGHT, PieceType.ROOK
    };

    public static ChessBoard createInitialBoard() {
        ChessBoard board = new ChessBoard();

        for (int i = 0; i < 8; i++) {
            char file = (char) ('a' + i);

            board = board.withPiece(Square.fromNotation(file + "1"), new Piece(BACK_RANK[i], PieceColor.WHITE));
            board = board.withPiece(Square.fromNotation(file + "2"), new Piece(PieceType.PAWN, PieceColor.WHITE));
            board = board.withPiece(Square.fromNotation(file + "7"), new Piece(PieceType.PAWN, PieceColor.BLACK));
            board = board.withPiece(Square.fromNotation(file + "8"), new Piece(BACK_RANK[i], PieceColor.BLACK));
        }

        return board;
    }
}