package org.Chess.ConsoleGame;

import org.Chess.Application.GameService;
import org.Chess.Domain.BoardPrinter;
import org.Chess.Domain.Exception.IllegalMoveException;
import org.Chess.Domain.Game.GameId;
import org.Chess.Domain.Game.GameStatus;
import org.Chess.Domain.Game.MoveResult;
import org.Chess.Domain.Game.PlayerId;
import org.Chess.Domain.Model.PieceColor;
import org.Chess.Domain.Model.Position;
import org.Chess.Domain.Model.Square;
import org.Chess.Domain.NotYourTureException;

import java.util.Locale;
import java.util.Scanner;

public class ConsoleGame {
    private final GameService   service;
    private final Scanner scanner;

    public ConsoleGame(GameService service, Scanner scanner) {
        this.service = service;
        this.scanner = scanner;
    }

    public void run() {

        PlayerId white = new PlayerId("white");
        PlayerId black = new PlayerId("black");
        GameId id = service.createGame(white, black);

        System.out.println("Commands:  e2 e4  |  e7 e8 q ( for promotion)  |  moves e2  |  quit");

        while(true){
            Position position = service.getPosition(id);
            BoardPrinter.print(position.board());

            if (service.isOver(id)) {
                System.out.println("Game over: " + service.getStatus(id) + "  " + position.sidetoMove().opposite() + " WON");
                return;
            }
            if (service.getStatus(id) == GameStatus.CHECK) System.out.println("Check!");


            PlayerId current = position.sidetoMove() == PieceColor.WHITE ?  white : black;

            System.out.print(current.value() + " to move > ");
            String line = scanner.nextLine().trim();

            System.out.println(line);

            if(line.equalsIgnoreCase("quite"))return;

            try{
                if(line.toLowerCase().startsWith("moves ")){
                     showMove(id , line.toLowerCase().substring(6).trim());
                }
                ParsedMoves pm = MoveParser.parse(line);

                MoveResult result = service.makeMove(id , current , pm.from() , pm.to() , pm.promotion());

                result.captueredPiece().ifPresent(p ->
                        System.out.println("Captured: " + p.color() + " " + p.type()));

        } catch (IllegalMoveException | NotYourTureException | IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }

        }

    }

    private void showMove(GameId id , String piece) {
        Square from = Square.fromNotation(piece);
        var targets = service.getLegalMoves(id, from).stream()
                .map(m -> m.to().toNotation())
                .distinct()
                .toList();
        System.out.println(targets.isEmpty() ? "No legal moves from " + piece : "Can go to: " + targets);
    }

}
