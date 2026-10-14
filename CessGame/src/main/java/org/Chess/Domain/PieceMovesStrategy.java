package org.Chess.Domain;

import java.util.List;
import java.util.Map;

//this class will help that which piece is there and which legal moves it can do
public class PieceMovesStrategy {

    private final Map<PieceType , PseudoLegalMoves> STRATERGIES = Map.of(
            PieceType.KNIGHT, new KnightMoves(),
            PieceType.ROOK,   new RookMoves(),
            PieceType.BISHOP, new BishopMoves(),
            PieceType.QUEEN,  new QueenMoves(),
            PieceType.KING,   new KingMoves(),
            PieceType.PAWN,   new PawnMoves()
    );


    public List<Square> movesFrom(ChessBoard board , Square square){

        Piece piece = board.pieceAt(square)
                .orElseThrow(()-> new IllegalArgumentException("no Peice at " + square.toNotation()));

       return  STRATERGIES.get(piece.type()).legalMoves(square , board  , piece.color());

    }
}
