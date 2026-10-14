package org.Chess.Domain;

public record CastlingRights(
        boolean whiteKingside,
        boolean whiteQueenside,
        boolean blackKingside,
        boolean blackQueenside
) {
    public static CastlingRights initial() {
        return new CastlingRights(
                true,
                true,
                true,
                true
        );
    }
}
