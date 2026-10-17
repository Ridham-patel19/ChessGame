package org.Chess.Domain.Moves;

import org.Chess.Domain.Model.ChessBoard;
import org.Chess.Domain.Model.Piece;
import org.Chess.Domain.Model.PieceColor;
import org.Chess.Domain.Model.Square;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PawnMoves implements PseudoLegalMoves {

    @Override
    public List<Square> legalMoves(Square from, ChessBoard board, PieceColor color) {
        List<Square> moves = new ArrayList<Square>();


        //cuz it go just forward but oaccoridng to color we have to change ditrection whether + or -
        int direction = (color == PieceColor.WHITE) ? 1 : -1;

        int row = from.row();
        int col = from.column();

        int oneStepRow = row + direction;

        if(oneStepRow >= 0 && oneStepRow <= 7){

            Square oneStep = new Square(oneStepRow , col);

            if(board.pieceAt(oneStep).isEmpty()){
                moves.add(oneStep);

                boolean isStartingRow =
                        (color == PieceColor.WHITE && row == 1) ||
                                (color == PieceColor.BLACK && row == 6);

                if(isStartingRow){

                    int twoStepRow = row + (2 * direction);

                    Square twoStep = new Square(twoStepRow , col);

                    if(board.pieceAt(twoStep).isEmpty()){
                        moves.add(twoStep);
                    }
                }
            }
        }

        int captureRow = row + direction;

        if(captureRow >= 0 && captureRow <= 7){

            int leftColumn = col - 1;

            if(leftColumn >= 0){

                Square leftCapture = new Square(captureRow , leftColumn);

                Optional<Piece> piece = board.pieceAt(leftCapture);

                if(piece.isPresent() && piece.get().color() != color){
                    moves.add(leftCapture);
                }
            }

            int rightColumn = col + 1;

            if(rightColumn <= 7){

                Square rightCapture = new Square(captureRow , rightColumn);

                Optional<Piece> piece = board.pieceAt(rightCapture);

                if(piece.isPresent() && piece.get().color() != color){
                    moves.add(rightCapture);
                }
            }
        }

        return moves;
    }
}