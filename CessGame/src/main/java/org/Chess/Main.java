
package org.Chess;

import org.Chess.Application.GameNotFoundException;
import org.Chess.Application.GameRepository;
import org.Chess.Application.GameService;
import org.Chess.ConsoleGame.ConsoleGame;
import org.Chess.Domain.Exception.IllegalMoveException;
import org.Chess.Domain.Game.GameId;
import org.Chess.Domain.Game.GameStatus;
import org.Chess.Domain.Game.MoveResult;
import org.Chess.Domain.Game.PlayerId;
import org.Chess.Domain.Model.PieceColor;
import org.Chess.Domain.Model.Square;
import org.Chess.Domain.Moves.AttackDetecter;
import org.Chess.Domain.Moves.LegalMovsGenerator;
import org.Chess.Domain.Moves.Move;
import org.Chess.Domain.NotYourTureException;
import org.Chess.Domain.Rules.GameStatusEvaluator;
import org.Chess.Domain.NotYourTureException;
import org.Chess.Infrastructure.InMemoryGameRepository;

import java.util.List;

public class Main {

    public static void main(String[] args) {


        System.out.println("Welcome to chess game ");
        LegalMovsGenerator legalMoveGenerator = new LegalMovsGenerator();
        GameStatusEvaluator evaluator = new GameStatusEvaluator(new AttackDetecter() , legalMoveGenerator);
        GameRepository repository = new InMemoryGameRepository();
        GameService service = new GameService(repository, legalMoveGenerator, evaluator);
        new ConsoleGame(service, new java.util.Scanner(System.in)).run();

    }
}
