package org.Chess.Domain.Game;

import org.Chess.Domain.Exception.IllegalMoveException;
import org.Chess.Domain.Model.*;
import org.Chess.Domain.Moves.LegalMovsGenerator;
import org.Chess.Domain.Moves.Move;
import org.Chess.Domain.Moves.MoveType;
import org.Chess.Domain.Rules.GameStatusEvaluator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class Game {

    private GameId gameId;
    private PlayerId whitePlayer;
    private PlayerId blackPlayer;
    private LegalMovsGenerator generator;
    private GameStatusEvaluator evaluator;

    private Position position;
    private GameStatus status;
    private List<Move> history = new ArrayList<>();


    public Game(GameId id, PlayerId whitePlayer, PlayerId blackPlayer,
                LegalMovsGenerator legalMoves, GameStatusEvaluator evaluator) {
        this.gameId = id;
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;
        this.generator = legalMoves;
        this.evaluator = evaluator;
        this.position = Position.initial();
        this.status = evaluator.evaluate(position);
    }

    public MoveResult play(Square from , Square to , PieceType promotion){
        if(isOver()){
             throw new IllegalMoveException("the Game is Over " + status);
        }

        Move move = generator.allLegalMove(position).stream().
                filter(m-> m.from().equals(from) && m.to().equals(to)  && m.promotion() == promotion ).
                findFirst().
                orElseThrow(() -> new IllegalMoveException(
                "Illegal move: " + from.toNotation() + " to " + to.toNotation()));

        Optional<Piece> capturedPiece = capturePiece(move);
        position = position.apply(move);
        status = evaluator.evaluate(position);
        history.add(move);
        return new MoveResult(move, capturedPiece, status, position.sidetoMove());


    }

    private Optional<Piece> capturePiece(Move move) {

        if(move.type() == MoveType.EN_PASSANT){
            return position.board().pieceAt(new Square(move.from().row() , move.to().column()));
        }

        return position.board().pieceAt(move.to());
    }


    public PlayerId playerToMove(){
        return this.position.sidetoMove() == PieceColor.WHITE  ? whitePlayer : blackPlayer;
    }
    public boolean isOver() {
        return status != GameStatus.CHECK && status != GameStatus.ONGOING;


    }

    public GameId id() { return gameId; }
    public Position position() { return position; }
    public GameStatus status() { return status; }
    public List<Move> history() { return List.copyOf(history); }   // read-only copy, outsiders can't change it


    public GameId getGameId() {
        return gameId;
    }

    public PlayerId getWhitePlayer() {
        return whitePlayer;
    }

    public PlayerId getBlackPlayer() {
        return blackPlayer;
    }

    public LegalMovsGenerator getGenerator() {
        return generator;
    }

    public GameStatusEvaluator getEvaluator() {
        return evaluator;
    }

    public Position getPosition() {
        return position;
    }

    public GameStatus getStatus() {
        return status;
    }

    public List<Move> getHistory() {
        return history;
    }
}
