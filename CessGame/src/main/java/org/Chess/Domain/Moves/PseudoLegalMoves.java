package org.Chess.Domain.Moves;

import org.Chess.Domain.Model.ChessBoard;
import org.Chess.Domain.Model.PieceColor;
import org.Chess.Domain.Model.Square;

import java.util.List;

public interface PseudoLegalMoves {

    //it will give us all the legal moves 
    List<Square> legalMoves (Square from , ChessBoard board , PieceColor color);
}
