package org.Chess.Domain.Rules;

import org.Chess.Domain.Model.ChessBoard;
import org.Chess.Domain.Model.PieceColor;
import org.Chess.Domain.Model.Position;
import org.Chess.Domain.Model.Square;
import org.Chess.Domain.Moves.AttackDetecter;
import org.Chess.Domain.Moves.Move;
import org.Chess.Domain.Moves.MoveType;

import java.util.ArrayList;
import java.util.List;

public class CastlingRules {

    private final AttackDetecter detect;


    public CastlingRules(AttackDetecter detect) {
        this.detect = detect;
    }


    public List<Move> castlingMoves(Position position){
        List<Move> moves = new ArrayList<>();
        PieceColor color = position.sidetoMove();
        int row = color == PieceColor.WHITE ? 0 : 7;
        //current king sqaure
        Square e = new Square(row , 4);

        if(canCastleKingSide(position , color)){
            moves.add(new Move(e , new Square(row , 6), null , MoveType.CASTLE ));
        }

        if(canCastleQueenside(position , color)){
            moves.add(new Move(e , new Square(row , 2), null , MoveType.CASTLE ));
        }


return moves;



    }

    public boolean canCastleKingSide(Position position , PieceColor color){

        boolean hasRight = color == PieceColor.WHITE ?
                position.castleRight().whiteKingside() :
                position.castleRight().blackKingside();

        if(!hasRight) return false;

        int row = color == PieceColor.WHITE ? 0 : 7;

        Square kingCurrentSquare =   new Square(row , 4);
        Square fSquare = new Square(row, 5);
        Square gSquare = new Square(row, 6);


        return isSqure(position.board(), fSquare, gSquare)
                && kingPathSafe(position.board(), color, kingCurrentSquare, fSquare, gSquare);


    }

    public boolean canCastleQueenside(Position position, PieceColor color) {
        boolean hasRight = color == PieceColor.WHITE
                ? position.castleRight().whiteQueenside()
                : position.castleRight().blackQueenside();

        if (!hasRight) return false;

        int row = color == PieceColor.WHITE ? 0 : 7;
        Square kingStart = new Square(row, 4);
        Square dSquare = new Square(row, 3);
        Square cSquare = new Square(row, 2);
        Square bSquare = new Square(row, 1);

        return isSqure(position.board(), dSquare, cSquare, bSquare)
                && kingPathSafe(position.board(), color, kingStart, dSquare, cSquare);
    }

    //... is varargs it takes all Sqaure type and make a list as many as Sqaure

    private boolean isSqure(ChessBoard board, Square... squars) {
        for (Square square : squars) {
            if (board.pieceAt(square).isPresent()) {
                return false;
            }
        }
        return true;
    }


    private boolean kingPathSafe(ChessBoard board , PieceColor color , Square... square){
        PieceColor enemyColor = color.opposite();

        for(Square s : square){
            if(detect.isAttacked(board , s , enemyColor)){
                return false;
            }

        }
        return true;
    }
}
