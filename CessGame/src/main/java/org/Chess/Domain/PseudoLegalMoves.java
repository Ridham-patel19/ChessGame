package org.Chess.Domain;

import java.util.List;

public interface PseudoLegalMoves {

    //it will give us all the legal moves 
    List<Square> legalMoves (Square from , ChessBoard board , PieceColor color);
}
