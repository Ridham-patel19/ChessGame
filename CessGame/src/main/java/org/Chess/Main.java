
package org.Chess;

import org.Chess.Application.GameNotFoundException;
import org.Chess.Application.GameRepository;
import org.Chess.Application.GameService;
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

        System.out.println("========== PHASE 9: APPLICATION + REPOSITORY TESTS ==========");

        // =====================================================
        // SETUP: COMPOSITION ROOT
        // =====================================================

        AttackDetecter attackDetector = new AttackDetecter();

        LegalMovsGenerator legalMoveGenerator =
                new LegalMovsGenerator();

        GameStatusEvaluator evaluator =
                new GameStatusEvaluator(
                        attackDetector,
                        legalMoveGenerator
                );

        GameRepository repository =
                new InMemoryGameRepository();

        GameService service =
                new GameService(
                        repository,
                        legalMoveGenerator,
                        evaluator
                );

        PlayerId alice = new PlayerId("alice");
        PlayerId bob = new PlayerId("bob");

        // =====================================================
        // TEST 1: CREATE A GAME
        // =====================================================

        System.out.println("\n--- TEST 1: Create Game ---");

        GameId id = service.createGame(alice, bob);

        System.out.println("Created game ID: " + id.value());

        check("Game ID was created", id != null);

        check("Game can be retrieved",
                service.getPosition(id) != null);

        System.out.println("Initial side to move: "
                + service.getPosition(id).sidetoMove());

        check("White moves first",
                service.getPosition(id).sidetoMove()
                        == PieceColor.WHITE);

        // =====================================================
        // TEST 2: ALICE MAKES A LEGAL MOVE
        // e2 -> e4
        // =====================================================

        System.out.println("\n--- TEST 2: Legal Move by Alice ---");

        MoveResult result = service.makeMove(
                id,
                alice,
                Square.fromNotation("e2"),
                Square.fromNotation("e4"),
                null
        );

        System.out.println("Move status: " + result.status());
        System.out.println("Next side: " + result.nextToMove());

        check("Move was accepted",
                result.status() == GameStatus.ONGOING);

        check("Black moves next",
                result.nextToMove() == PieceColor.BLACK);

        check("Position was updated",
                service.getPosition(id).board()
                        .pieceAt(Square.fromNotation("e4"))
                        .isPresent());

        // =====================================================
        // TEST 3: ALICE TRIES TO MOVE AGAIN
        // Expected: NotYourTurnException
        // =====================================================

        System.out.println("\n--- TEST 3: Wrong Player's Turn ---");

        try {
            service.makeMove(
                    id,
                    alice,
                    Square.fromNotation("d2"),
                    Square.fromNotation("d4"),
                    null
            );

            check("Wrong-turn move is rejected", false);

        } catch (NotYourTureException exception) {
            System.out.println("[PASS] " + exception.getMessage());
        }

        // =====================================================
        // TEST 4: BOB MAKES AN ILLEGAL MOVE
        // e7 -> e3 is not a legal pawn move
        // Expected: IllegalMoveException
        // =====================================================

        System.out.println("\n--- TEST 4: Illegal Move by Bob ---");

        try {
            service.makeMove(
                    id,
                    bob,
                    Square.fromNotation("e7"),
                    Square.fromNotation("e3"),
                    null
            );

            check("Illegal move is rejected", false);

        } catch (IllegalMoveException exception) {
            System.out.println("[PASS] " + exception.getMessage());
        }

        // =====================================================
        // TEST 5: UNKNOWN GAME ID
        // Expected: GameNotFoundException
        // =====================================================

        System.out.println("\n--- TEST 5: Unknown Game ---");

        try {
            service.getPosition(GameId.newId());

            check("Unknown game is rejected", false);

        } catch (GameNotFoundException exception) {
            System.out.println("[PASS] " + exception.getMessage());
        }

        // =====================================================
        // TEST 6: LEGAL MOVES FOR BLACK'S e7 PAWN
        // Expected destinations: e6 and e5
        // =====================================================

        System.out.println("\n--- TEST 6: Black's Legal Moves ---");

        List<Move> legalMoves = service.getLegalMoves(
                id,
                Square.fromNotation("e7")
        );

        System.out.println("Legal moves from e7:");

        for (Move move : legalMoves) {
            System.out.println(
                    move.from().toNotation()
                            + " -> "
                            + move.to().toNotation()
            );
        }

        boolean canMoveE6 = legalMoves.stream()
                .anyMatch(move ->
                        move.to().equals(Square.fromNotation("e6")));

        boolean canMoveE5 = legalMoves.stream()
                .anyMatch(move ->
                        move.to().equals(Square.fromNotation("e5")));

        check("e7 -> e6 is legal", canMoveE6);
        check("e7 -> e5 is legal", canMoveE5);

        // =====================================================
        // TEST 7: REPOSITORY RETAINS THE UPDATED GAME
        // =====================================================

        System.out.println("\n--- TEST 7: Repository Verification ---");

        check("Game remains retrievable after move",
                service.getPosition(id) != null);

        check("Updated board is retained",
                service.getPosition(id).board()
                        .pieceAt(Square.fromNotation("e4"))
                        .isPresent());

        System.out.println("\n========== PHASE 9 TESTS COMPLETED ==========");
    }

    private static void check(String description, boolean passed) {
        System.out.println(
                (passed ? "[PASS] " : "[FAIL] ") + description
        );
    }
}
