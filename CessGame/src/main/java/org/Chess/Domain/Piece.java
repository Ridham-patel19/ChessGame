package org.Chess.Domain;

public record Piece(PieceType type , PieceColor color) {

    public Piece {
        if (type == null || color == null) {
            throw new IllegalArgumentException("Piece needs a type and a color");
        }
    }
}
