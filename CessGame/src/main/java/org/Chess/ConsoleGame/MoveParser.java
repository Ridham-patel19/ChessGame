package org.Chess.ConsoleGame;

import org.Chess.Domain.Model.PieceType;
import org.Chess.Domain.Model.Square;

import java.util.Locale;

public class MoveParser {
    public static ParsedMoves parse(String input){
        String[] parts = input.trim().toLowerCase().split("\\s+");

        System.out.println(parts.length);
        if(parts.length < 2 || parts.length > 3){
            throw new IllegalArgumentException("Type a move like: e2 e4   (promotion: e7 e8 q)");
        }

        Square from = Square.fromNotation(parts[0]);
        Square to = Square.fromNotation(parts[1]);
        PieceType promotion = parts.length == 3? getPromotionPiece(parts[2]):null;
        return new ParsedMoves(from, to, promotion);


    }

    private static PieceType getPromotionPiece(String piece){
        return switch (piece) {
            case "q" -> PieceType.QUEEN;
            case "r" -> PieceType.ROOK;
            case "b" -> PieceType.BISHOP;
            case "n" -> PieceType.KNIGHT;
            default -> throw new IllegalArgumentException("Promotion must be q, r, b or n");
        };
    }
}
