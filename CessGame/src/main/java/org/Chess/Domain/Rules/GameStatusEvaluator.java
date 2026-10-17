package org.Chess.Domain.Rules;

import org.Chess.Domain.Game.GameStatus;
import org.Chess.Domain.Model.*;
import org.Chess.Domain.Moves.AttackDetecter;
import org.Chess.Domain.Moves.LegalMovsGenerator;

public class GameStatusEvaluator {

    private AttackDetecter detect;
    private LegalMovsGenerator genrate;


    public GameStatusEvaluator(AttackDetecter detect, LegalMovsGenerator genrate) {
        this.detect = detect;
        this.genrate = genrate;
    }

    public GameStatus evaluate(Position position ){
        PieceColor color = position.sidetoMove();
        boolean inCheck = isInCheck(position , color);
        boolean hasMoves = !genrate.allLegalMove(position).isEmpty();

        if(!hasMoves){
            return inCheck ? GameStatus.CHECKMATE : GameStatus.STALEMATE;
        }
        if(isInsufficientMaterial(position.board())){
              return GameStatus.DRAW_INSUFFICIENT_MATERIAL;
        }

        if (position.halfMoveClock() >= 100) {      // 100 half-moves = 50 full moves
            return GameStatus.DRAW_FIFTY_MOVE;
        }
        return inCheck ? GameStatus.CHECK : GameStatus.ONGOING;
    }

    private boolean isInsufficientMaterial(ChessBoard board) {
        int numOfMinorPiece = 0;

        for (int row = 0 ; row < 8 ; row++){
            for (int col = 0 ; col < 8 ; col++){
                var p = board.pieceAt(new Square(row , col));

                if(p.isEmpty()){
                    continue;
                }

                PieceType t = p.get().type();

                if (t == PieceType.PAWN || t == PieceType.ROOK || t == PieceType.QUEEN) return false;
                if (t == PieceType.BISHOP || t == PieceType.KNIGHT) numOfMinorPiece++;


            }
        }

        return numOfMinorPiece <= 1;
    }

    private boolean isInCheck(Position position, PieceColor color) {
        Square king = position.board().findKing(color);

        return detect.isAttacked(position.board() , king , color.opposite());
    }
}
