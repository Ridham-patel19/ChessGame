package org.Chess.Domain.Moves;

import org.Chess.Domain.Model.ChessBoard;
import org.Chess.Domain.Model.Piece;
import org.Chess.Domain.Model.PieceColor;
import org.Chess.Domain.Model.Square;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BishopMoves implements PseudoLegalMoves {

    private static final int[][] OFFSETS = {
            {-1 , -1} , {1 , 1} , {-1, 1} , {1 , -1}
    };

    @Override
    public List<Square> legalMoves(Square from, ChessBoard board, PieceColor color) {
        List<Square> moves = new ArrayList<Square>();

        for(int[] offset : OFFSETS){
            int row = from.row();
            int col = from.column();

            while(true){
                row += offset[0];
                col += offset[1];

                if(row < 0 || row > 7 || col < 0 || col > 7){
                    break;
                }

                Square currentPlace = new Square(row , col);

                Optional<Piece> piece = board.pieceAt(currentPlace);

                if(piece.isEmpty()){
                    moves.add(currentPlace);
                    continue;
                }

                if(piece.get().color() == color){
                    break;
                }else{
                    moves.add(currentPlace);
                    break;
                }
            }
        }

        return moves;
    }
}
