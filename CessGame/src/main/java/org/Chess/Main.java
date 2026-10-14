
package org.Chess;

import org.Chess.Domain.*;

public class Main {

    public static void main(String[] args) {

        // Create the dependencies
        AttackDetecter attackDetector = new AttackDetecter();

        LegalMovsGenerator legalMoveGenerator =
                new LegalMovsGenerator();

        GameStatusEvaluator evaluator =
                new GameStatusEvaluator(
                        attackDetector,
                        legalMoveGenerator

                );

        // Create a new game
        Game game = new Game(
                GameId.newId(),
                new PlayerId("alice"),
                new PlayerId("bob"),
                legalMoveGenerator,
                evaluator
        );

        System.out.println("========== PHASE 9: GAME TEST ==========");

        // TEST 1: Initial game state
        System.out.println("\n--- TEST 1: Initial State ---");

        System.out.println("Game ID: " + game.id().value());
        System.out.println("Initial status: " + game.status());
        System.out.println("Player to move: "
                + game.playerToMove().value());
        System.out.println("Side to move: "
                + game.position().sidetoMove());

        // TEST 2: Play Fool's Mate
        System.out.println("\n--- TEST 2: Play Fool's Mate ---");

        playMove(game, "f2", "f3");
        playMove(game, "e7", "e5");
        playMove(game, "g2", "g4");

        MoveResult result = game.play(
                Square.fromNotation("d8"),
                Square.fromNotation("h4"),
                null
        );

        System.out.println("Move: d8 -> h4");
        System.out.println("Move result status: " + result.status());
        System.out.println("Next side to move: " + result.nextToMove());
        System.out.println("Game status: " + game.status());
        System.out.println("Game over: " + game.isOver());

        // TEST 3: Verify checkmate
        System.out.println("\n--- TEST 3: Checkmate Verification ---");

        check(
                "Game status is CHECKMATE",
                game.status() == GameStatus.CHECKMATE
        );

        check(
                "Game is over",
                game.isOver()
        );

        check(
                "History contains 4 moves",
                game.history().size() == 4
        );

        check(
                "White is the side to move after checkmate",
                game.position().sidetoMove() == PieceColor.WHITE
        );

        // TEST 4: Verify playerToMove
        System.out.println("\n--- TEST 4: Player Verification ---");

        System.out.println("Player to move: "
                + game.playerToMove().value());

        check(
                "Alice is White and White is to move",
                game.playerToMove().value().equals("alice")
        );

        // TEST 5: Attempt another move after checkmate
        System.out.println("\n--- TEST 5: Move After Game Over ---");

        try {
            game.play(
                    Square.fromNotation("e2"),
                    Square.fromNotation("e4"),
                    null
            );

            System.out.println("[FAIL] Move should have been rejected");

        } catch (IllegalMoveException exception) {
            System.out.println("[PASS] Move rejected: "
                    + exception.getMessage());
        }

        // TEST 6: Print move history
        System.out.println("\n--- TEST 6: Move History ---");

        int moveNumber = 1;

        for (Move move : game.history()) {
            System.out.println(
                    moveNumber++ + ". "
                            + move.from().toNotation()
                            + " -> "
                            + move.to().toNotation()
            );
        }

        System.out.println("\n========== TESTS COMPLETED ==========");
    }

    private static void playMove(
            Game game,
            String from,
            String to
    ) {
        MoveResult result = game.play(
                Square.fromNotation(from),
                Square.fromNotation(to),
                null
        );

        System.out.println(
                from + " -> " + to
                        + " | Status: " + result.status()
                        + " | Next: " + result.nextToMove()
                        + " | Player: "
                        + game.playerToMove().value()
        );
    }

    private static void check(
            String description,
            boolean passed
    ) {
        System.out.println(
                (passed ? "[PASS] " : "[FAIL] ") + description
        );
    }
}
