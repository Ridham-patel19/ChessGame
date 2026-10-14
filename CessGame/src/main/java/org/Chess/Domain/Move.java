package org.Chess.Domain;

public record Move(Square from , Square to , PieceType promotion , MoveType type) {
    public static Move of(Square from , Square to){
        return new  Move(
                from , to , null , MoveType.NORMAL
        );
    }
}
