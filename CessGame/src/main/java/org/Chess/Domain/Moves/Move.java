package org.Chess.Domain.Moves;

import org.Chess.Domain.Model.PieceType;
import org.Chess.Domain.Model.Square;

public record Move(Square from , Square to , PieceType promotion , MoveType type) {
    public static Move of(Square from , Square to){
        return new  Move(
                from , to , null , MoveType.NORMAL
        );
    }
}
