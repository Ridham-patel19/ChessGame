package org.Chess.Domain;

import java.util.Optional;

public class ChessBoard {

    private final Piece[][] chessBoard ;

    public ChessBoard() {

        this.chessBoard = new Piece[8][8];
    }
   private ChessBoard(Piece[][] board){
        this.chessBoard = board;
   }


   //its return that which Piece exist in that square
   public Optional<Piece> pieceAt(Square s){
        return Optional.ofNullable(chessBoard[s.row()][s.column()]);
   }


   //its return that is sqaure is empty or not
    public boolean isEmpty(Square s){
        return chessBoard[s.row()][s.column()] == null;
    }

    //imutable chessboard return new board with new change
    public ChessBoard withPiece( Square s, Piece Piece){
                  Piece[][] copy = copyBoard();
                  copy[s.row()][s.column()] = Piece;
                  return new ChessBoard(copy);
    }

    private Piece[][] copyBoard() {
        Piece[][] newBoard = new Piece[8][8];
        for(int i = 0 ; i < 8 ; i++){
            //clone copy whole object here obj is whole array of Piece
            newBoard[i] = chessBoard[i].clone();
        }
        return newBoard;
    }

    //returns new empty board without changed Piece
    public ChessBoard withoutPiece( Square s){
        return withPiece(s , null);
    }

    // inside ChessBoard.java
    public Square findKing(PieceColor color) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Square s = new Square(row, col);
                var piece = pieceAt(s);
                if (piece.isPresent() && piece.get().type() == PieceType.KING && piece.get().color() == color) {
                    return s;
                }
            }
        }
        throw new IllegalStateException("No " + color + " king on board");
    }

}
