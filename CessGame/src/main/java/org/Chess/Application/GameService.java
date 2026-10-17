package org.Chess.Application;

import org.Chess.Domain.Game.Game;
import org.Chess.Domain.Game.GameId;
import org.Chess.Domain.Game.MoveResult;
import org.Chess.Domain.Game.PlayerId;
import org.Chess.Domain.Model.PieceType;
import org.Chess.Domain.Model.Position;
import org.Chess.Domain.Model.Square;
import org.Chess.Domain.Moves.LegalMovsGenerator;
import org.Chess.Domain.Moves.Move;
import org.Chess.Domain.NotYourTureException;
import org.Chess.Domain.Rules.GameStatusEvaluator;

import java.util.List;

public class GameService {
    private final GameRepository repository;
    private final LegalMovsGenerator legalMoves;
    private final GameStatusEvaluator evaluator;

    public GameService(GameRepository repository, LegalMovsGenerator legalMoves,
                       GameStatusEvaluator evaluator) {
        this.repository = repository;
        this.legalMoves = legalMoves;
        this.evaluator = evaluator;
    }


    public GameId createGame(PlayerId white , PlayerId black){
     Game game = new Game(GameId.newId(),white , black , legalMoves , evaluator);
     repository.save(game);
     return game.id();
    }

    public MoveResult makeMove(GameId gameId , PlayerId player ,
                               Square from , Square to , PieceType promotion){


        Game game = load(gameId);

        if(!game.playerToMove().equals(player))
            throw new NotYourTureException(("It is not your " + player.value() + " Turn"));
        MoveResult result = game.play(from , to , promotion);
        repository.save(game);
        return result;
    }

    public List<Move> getLegalMoves(GameId gameId, Square from) {
        return load(gameId).
                getGenerator().
                legalMove(getPosition(gameId) , from);
    }

    public Position getPosition(GameId gameId) { return load(gameId).position(); }

    private Game load(GameId id) {
        return repository.findById(id).orElseThrow(() -> new GameNotFoundException(id));
    }
}




