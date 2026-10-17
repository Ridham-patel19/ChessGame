package org.Chess.Domain.Model;

import org.Chess.Domain.Moves.Move;
import org.Chess.Domain.Moves.MoveType;

import java.awt.dnd.InvalidDnDOperationException;

/*
 * For every move, we will have a new Position.
 * The Position represents the current state of the chess game after that move.
 */

public record Position(
        ChessBoard board,
        PieceColor sidetoMove,
        CastlingRights castleRight,
        Square enPassant,
        int halfMoveClock
) {

    /*
     * When we start the game, we will initialize the first Position
     * with this static initial method.
     * It will initialize the first board, White has to play first,
     * the initial castling rights, no en passant target, and the half move clock as 0.
     */
    public static Position initial(){
        return new Position(
                BoardFactory.createInitialBoard(),
                PieceColor.WHITE,
                CastlingRights.initial(),
                null,
                0
        );
    }


    public Position apply(Move move){

        /*
         * If the move is Castle, En Passant, or Promotion,
         * we haven't made this yet.
         * That's why we are throwing an exception here.
         * When we will build it, we will add the code here.
         */
        if( move.type() == MoveType.PROMOTION){
            throw new InvalidDnDOperationException("We havenot implemented yet");


        }

        if(move.type() == MoveType.CASTLE){
            return applyCastle(move);
        }

        if(move.type() == MoveType.EN_PASSANT){
            return applyEnPassant(move);
        }
        Piece movingPiece = board.pieceAt(move.from())
                .orElseThrow(() -> new IllegalStateException("No piece on " + move.from().toNotation()));

        /*
         * The legal move generator is giving all the legal moves,
         * but at the time of a capture, we have to check where the piece is going.
         * On that square, is there any piece or not.
         * That's why we are checking here.
         * If there is a piece, then only it can capture.
         */
        boolean isCapture = board.pieceAt(move.to()).isPresent();

        boolean isPawnMove = movingPiece.type() == PieceType.PAWN;

        ChessBoard newBoard = board.withoutPiece(move.from()).withPiece(move.to() , movingPiece);

        /*
         * For every move, we have to create a new Position.
         * We use the apply method through the Position object which we first created.
         * From that object, we get the previous values like the half move clock
         * and create a new Position.
         * If the half move clock has to be incremented, it will be incremented
         * from the value of the previous Position.
         * For the next move, we will again use the new Position object
         * and call the apply method on that object.
         */




        return new Position(
                newBoard,
                movingPiece.color().opposite(),
                nextCastlingRights(movingPiece, move),
                nextEnPassantTarget(movingPiece, move),
                nextHalfmoveClock(isPawnMove, isCapture)
        );

    }


    private Position applyEnPassant(Move move) {
        Piece pawn = board.pieceAt(move.from()).orElseThrow();

        Square capturedSquare = new Square(move.from().row() , move.to().column());

        ChessBoard newBoard = board
                .withoutPiece(move.from())
                .withoutPiece(capturedSquare)
                .withPiece(move.to() , pawn);

        return new Position(
                newBoard,
                sidetoMove.opposite(),
                castleRight,
                null,
                0
        );
    }

    private Position applyCastle(Move move){
        Piece king = board.pieceAt(move.from()).orElseThrow();
        int row = king.color() == PieceColor.WHITE ? 0 : 7;
        boolean isKingSide = move.to().column() == 6;

        Square fromRook = new Square(row, isKingSide ? 7 : 0);
        Square toRook = new Square(row, isKingSide ? 5 : 3);

        Piece rook = board.pieceAt(fromRook).orElseThrow();

        ChessBoard newboard = board.withoutPiece(fromRook).
                withoutPiece(move.from()).
                withPiece(move.to() , king).
                withPiece(toRook , rook);

        return new Position(newboard ,
                king.color().opposite(),
                nextCastlingRights(king , move),
                null,
                halfMoveClock + 1);



    }
    private Square nextEnPassantTarget(Piece movingPiece ,Move move){

        /*
         * Math.abs is a method which is used for absolute value.
         * It makes a negative value positive, so we can check
         * whether the pawn moved two squares in either direction.
         */
        boolean isPawnMovedTwoSqaure = movingPiece.type() == PieceType.PAWN &&
                Math.abs(move.from().row() - move.to().row()) == 2;

        if (!isPawnMovedTwoSqaure) {
            return null; // en passant is only available for the ONE move right after a two-square advance
        }

        /*
         * If the pawn moved two squares, then we calculate the row
         * between the from square and the to square.
         * The column will be the same because the pawn moved two squares
         * forward, so we use the same column from the from square.
         */
        int pssedRow = (move.from().row() + move.to().row()) / 2;

        return new Square(pssedRow , move.from().column());
    }

    // this method is just checking if castling right is ther eor not
    private CastlingRights nextCastlingRights(Piece movingPiece , Move move){
        boolean whiteKingside = castleRight.whiteKingside();
        boolean whiteQueenside = castleRight.whiteQueenside();
        boolean blackKingside = castleRight.blackKingside();
        boolean blackQueenside = castleRight.blackQueenside();



        //if king is moved
        if(movingPiece.type() == PieceType.KING){
            if(movingPiece.color() == PieceColor.WHITE){
                whiteKingside = false ;
                whiteQueenside = false;
            }else{
                blackKingside = false ;
                blackQueenside = false;
            }
        }


        // if moving sqaure is rooks square then
        if(isSquare(move.from() , "a1"))whiteQueenside = false;
        if (isSquare(move.from(), "h1")) whiteKingside = false;
        if (isSquare(move.from(), "a8")) blackQueenside = false;
        if (isSquare(move.from(), "h8")) blackKingside = false;


        // rook captured ON its starting square — lose that side's right too
        if (isSquare(move.to(), "a1")) whiteQueenside = false;
        if (isSquare(move.to(), "h1")) whiteKingside = false;
        if (isSquare(move.to(), "a8")) blackQueenside = false;
        if (isSquare(move.to(), "h8")) blackKingside = false;


        return new CastlingRights(whiteKingside, whiteQueenside, blackKingside, blackQueenside);
    }


    //its just check whether the given sqaure is same as string
    private boolean isSquare(Square square , String notation){
        return square.equals(Square.fromNotation(notation));
    }
    private int nextHalfmoveClock(boolean isPawnMove , boolean isCapture){

        /*
         * The half move clock is a field in the Position record.
         * When we create the first Position using the initial method,
         * the half move clock starts with 0.
         * For every new move, the apply method uses the current Position object,
         * so the half move clock gets its previous value from that object.
         * If a pawn is moved or something is captured, it becomes 0.
         * Otherwise, it increments the previous half move clock value by 1.
         */
        return (isCapture || isPawnMove) ? 0 : halfMoveClock + 1;
    }
}