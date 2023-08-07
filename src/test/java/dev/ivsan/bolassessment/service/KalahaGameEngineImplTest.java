package dev.ivsan.bolassessment.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ivsan.bolassessment.model.Board;
import dev.ivsan.bolassessment.model.GameState;
import dev.ivsan.bolassessment.model.Pit;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static dev.ivsan.bolassessment.utils.BoardUtils.BOARD_PITS_LENGTH;
import static dev.ivsan.bolassessment.utils.BoardUtils.encodeBoardState;
import static dev.ivsan.bolassessment.utils.TestDataGenerator.aBoard;
import static dev.ivsan.bolassessment.utils.TestDataGenerator.aPlayer;
import static dev.ivsan.bolassessment.utils.TestDataGenerator.singleMoveVictoryBoard;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class KalahaGameEngineImplTest {

    private final KalahaGameEngine kalahaGameEngine = new KalahaGameEngineImpl(
            new SerializationHelperImpl(new ObjectMapper())
    );

    @Test
    void shouldProcessAndLogMoves() {
        Board board = aBoard();
        board = kalahaGameEngine.processMove(board, 1);
        assertEquals("0-6.0.7.7.7.7.1.-7.6.6.6.6.6.0.", encodeBoardState(board));
        board = kalahaGameEngine.processMove(board, 1);
        assertEquals("1-7.0.7.7.7.7.1.-7.0.7.7.7.7.1.", encodeBoardState(board));
        board = kalahaGameEngine.processMove(board, 5);
        assertEquals("0-7.0.7.7.7.0.2.-8.1.8.8.8.8.1.", encodeBoardState(board));
        board = kalahaGameEngine.processMove(board, 1);
        assertEquals("1-7.0.7.7.7.0.2.-8.0.9.8.8.8.1.", encodeBoardState(board));
        board = kalahaGameEngine.processMove(board, 4);
        assertEquals("0-7.0.7.7.0.1.3.-9.1.10.9.9.8.1.", encodeBoardState(board));
        assertEquals(GameState.IN_PROGRESS, board.getState());
        assertEquals(List.of(1, 1, 5, 1, 4), board.getMoveLog());
    }

    @Test
    void shouldNotProcessMoveWithInvalidPitIndex() {
        assertThrows(IllegalArgumentException.class, () -> kalahaGameEngine.processMove(aBoard(), -1));
        assertThrows(IllegalArgumentException.class, () -> kalahaGameEngine.processMove(aBoard(), BOARD_PITS_LENGTH - 1));
    }

    @Test
    void shouldNotProcessMoveWithInvalidPitWithNoStones() {
        assertThrows(IllegalArgumentException.class, () -> kalahaGameEngine.processMove(singleMoveVictoryBoard(), 1));
    }

    @Test
    void shouldAchieveVictoryCondition() {
        Board board = singleMoveVictoryBoard();
        board = kalahaGameEngine.processMove(board, 5);
        assertEquals("1-0.0.0.0.0.0.1.-6.6.6.6.6.6.0.", encodeBoardState(board));
        assertEquals(GameState.NORTH_WIN, board.getState());
    }

    @Test
    void shouldSkipOpponentsBigPitWhileSowing() {
        Board board = Board.builder()
                .northPlayer(aPlayer())
                .northPits(
                        Stream.concat(
                                Stream.generate(Pit::new).limit(BOARD_PITS_LENGTH - 1),
                                Stream.generate(() -> new Pit(0, Pit.PitType.BIG)).limit(1)
                        ).collect(Collectors.toList())
                )
                .southPlayer(aPlayer())
                .southPits(List.of(
                        new Pit(1, Pit.PitType.REGULAR),
                        new Pit(1, Pit.PitType.REGULAR),
                        new Pit(1, Pit.PitType.REGULAR),
                        new Pit(1, Pit.PitType.REGULAR),
                        new Pit(1, Pit.PitType.REGULAR),
                        new Pit(10, Pit.PitType.REGULAR),
                        new Pit(0, Pit.PitType.BIG)
                ))
                .build();
        board = kalahaGameEngine.processMove(board, 5);
        assertEquals("0-2.2.2.1.1.0.1.-7.7.7.7.7.7.0.", encodeBoardState(board));
    }

    @Test
    void shouldGetAnotherTurnWhenEndInOwnBigPit() {
        Board board = aBoard();
        board = kalahaGameEngine.processMove(board, 0);
        assertEquals("1-0.7.7.7.7.7.1.-6.6.6.6.6.6.0.", encodeBoardState(board));
    }

    @Test
    void shouldCaptureOpponentStonesWhileEndInOwnRegularEmptyPit() {
        Board board = Board.builder()
                .northPlayer(aPlayer())
                .northPits(
                        Stream.concat(
                                Stream.generate(Pit::new).limit(BOARD_PITS_LENGTH - 1),
                                Stream.generate(() -> new Pit(0, Pit.PitType.BIG)).limit(1)
                        ).collect(Collectors.toList())
                )
                .southPlayer(aPlayer())
                .southPits(List.of(
                        new Pit(1, Pit.PitType.REGULAR),
                        new Pit(0, Pit.PitType.REGULAR),
                        new Pit(1, Pit.PitType.REGULAR),
                        new Pit(1, Pit.PitType.REGULAR),
                        new Pit(1, Pit.PitType.REGULAR),
                        new Pit(1, Pit.PitType.REGULAR),
                        new Pit(0, Pit.PitType.BIG)
                ))
                .build();
        board = kalahaGameEngine.processMove(board, 0);
        assertEquals("0-0.0.1.1.1.1.7.-6.6.6.6.0.6.0.", encodeBoardState(board));
    }

}