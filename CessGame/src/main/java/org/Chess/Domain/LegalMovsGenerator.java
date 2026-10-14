package org.Chess.Domain;

import java.util.ArrayList;
import java.util.List;

public class LegalMovsGenerator {

    private final AttackDetecter attackDetector = new AttackDetecter();
    private  final PieceMovesStrategy PseudoLegalMoves = new PieceMovesStrategy();


    private final CastlingRules rules = new CastlingRules(attackDetector);


//its generating moves for all pieces in the board for speecific color
    public List<Move> allLegalMove(Position position){

        List<Move> allMoves = new ArrayList<>();
          for(int row = 0 ; row < 8 ; row++){
              for(int col = 0 ; col < 8 ; col++ ){
                      Square s = new Square(row , col);

                      boolean isOwnPieceIsSameColor = position.board().
                              pieceAt(s).
                              map(p -> p.color() == position.sidetoMove()).orElse(false);

                      if(isOwnPieceIsSameColor){
                          allMoves.addAll(legalMove(position , s));
                      }
              }
          }

          return allMoves;
    }



    public List<Move> legalMove(Position position, Square from) {

        List<Move> moves = new ArrayList<>();

        // Get the piece at the given square
        Piece piece = position.board().pieceAt(from).orElse(null);

        // No piece, or opponent's piece: no legal moves
        if (piece == null || piece.color() != position.sidetoMove()) {
            return moves;
        }

        // Generate regular legal moves
        for (Square square : legalMovesFrom(
                position.board(), from, position.sidetoMove())) {
            moves.add(Move.of(from, square));
        }

        // Add castling moves only for the king on its starting square
        if (piece.type() == PieceType.KING
                && from.equals(Square.fromNotation(
                piece.color() == PieceColor.WHITE ? "e1" : "e8"))) {
            moves.addAll(rules.castlingMoves(position));
        }

        // Add en passant moves
        moves.addAll(enPassantMove(position, from));

        return moves;
    }



private List<Move> enPassantMove(Position position , Square from){
    List<Move> moves = new ArrayList<>();

    Square target = position.enPassant();
    if(target == null){
        return moves;
    }

    PieceColor color = position.sidetoMove();

    Piece piece = position.board().pieceAt(from).orElse(null);

    if(
            piece == null || piece.type() != PieceType.PAWN || piece.color() != color
    ){
        return moves;
    }

    int direction = color == PieceColor.WHITE ? 1 : -1;

    boolean isRightDirection =
            target.row() == from.row() + direction &&
                    Math.abs(target.column() - from.column()) == 1;

    if(!isRightDirection)return moves;

    Move move = new Move(from , target , null , MoveType.EN_PASSANT);

    // legality: simulate it and make sure your own king isn't attacked afterwards
    Position after = position.apply(move);
    Square king = findKing(after.board(), color);
    if (!attackDetector.isAttacked(after.board(), king, color.opposite())) {
        moves.add(move);
    }
    return moves;
}

    public List<Square> legalMovesFrom( ChessBoard board , Square from , PieceColor color){
        List<Square> pseudoMoves = PseudoLegalMoves.movesFrom(board , from);
        List<Square> legal = new ArrayList<Square>();

        Piece piece = board.pieceAt(from)
                .orElseThrow(()-> new IllegalArgumentException("no piece at " + from.toNotation()));
        for(Square to : pseudoMoves){



            ChessBoard afterMove  = board
                    .withoutPiece(from)
                    .withoutPiece(to)
                    .withPiece(to, piece);

            Square king = findKing(afterMove , color);

            if (!attackDetector.isAttacked(afterMove, king, color.opposite())) {
                legal.add(to);
            }
        }

        return legal;
    }


    private Square findKing(ChessBoard board , PieceColor color){
        for(int row = 0 ; row < 8 ; row ++){
            for( int col = 0 ; col < 8 ; col++ ){
                Square square = new Square(row , col);
                var piece = board.pieceAt(square);

                if(piece.isPresent() && piece.get().color() == color && piece.get().type() == PieceType.KING){
                    return square;
                }
            }
        }
        throw new IllegalStateException("No " + color + " king on board");
    }

}
