package org.Chess.Domain.Model;

public record Piece(PieceType type , PieceColor color) {

    public Piece {
        if (type == null || color == null) {
            throw new IllegalArgumentException("Piece needs a type and a color");
        }
    }
}
