package org.Chess.Domain;

import java.sql.SQLInvalidAuthorizationSpecException;
import java.util.ArrayList;
import java.util.List;


//this particular function is taking three arguments one is a square then a board chess board
//then the colour of peace like which colour of peace is there in that particular square and now
//it will it will according to peace type it will check that whether this we are like from that square
//how manyc possible square are there where this particular piece can go it will also check whether
//the peace is not going outside the body
public class KnightMoves implements PseudoLegalMoves{

    private static final int[][] OFFSETS = {
            {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
            {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
    };
    @Override
    public List<Square> legalMoves(Square from, ChessBoard board, PieceColor color) {
        List<Square> moves = new ArrayList<Square>();
         for(int[] offset : OFFSETS){
             int introw = from.row() + offset[0];
             int intColumn = from.column() + offset[1];

             if(introw < 0 || introw > 7 || intColumn < 0 || intColumn > 7){
                 continue;
             }
              Square currentPlace = new Square(introw , intColumn);

             //here map is little diff from normal one here it gives lamda access of a piece
             // like pieceAt returns OPtion<> container in that there would be either null or Piece
             boolean isSameColor = board.pieceAt(currentPlace).map(p -> p.color() == color).orElse(false);
             if(!isSameColor){
                 moves.add(currentPlace);
             }
         }
  return moves;
    }
}
