package dev.ivsan.bolassessment.service;

import dev.ivsan.bolassessment.model.Board;
import dev.ivsan.bolassessment.model.GameState;
import org.junit.jupiter.api.Test;

import static dev.ivsan.bolassessment.utils.BoardUtils.DEFAULT_BOARD_PITS_LENGTH;
import static dev.ivsan.bolassessment.utils.BoardUtils.encodeBoardState;
import static dev.ivsan.bolassessment.utils.TestDataGenerator.aBoard;
import static dev.ivsan.bolassessment.utils.TestDataGenerator.singleMoveVictoryBoard;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class KalahaGameEngineImplTest {

    private final KalahaGameEngine kalahaGameEngine = new KalahaGameEngineImpl();

    @Test
    void testProcessMove() {
        Board board = aBoard();
        assertEquals("1-6.6.6.6.6.6.0.-6.6.6.6.6.6.0.", encodeBoardState(board));
        kalahaGameEngine.processMove(board, 1);
        assertEquals("0-6.0.7.7.7.7.1.-7.6.6.6.6.6.0.", encodeBoardState(board));
        kalahaGameEngine.processMove(board, 1);
        assertEquals("1-7.0.7.7.7.7.1.-7.0.7.7.7.7.1.", encodeBoardState(board));
        kalahaGameEngine.processMove(board, 5);
        assertEquals("0-7.0.7.7.7.0.2.-8.1.8.8.8.8.1.", encodeBoardState(board));
        kalahaGameEngine.processMove(board, 1);
        assertEquals("0-7.0.7.7.7.0.2.-8.0.9.8.8.8.1.", encodeBoardState(board));
        assertEquals(GameState.IN_PROGRESS, board.getState());
    }

    @Test
    void testProcessMoveInvalidPitIndex() {
        assertThrows(IllegalArgumentException.class, () -> kalahaGameEngine.processMove(aBoard(), -1));
        assertThrows(IllegalArgumentException.class, () -> kalahaGameEngine.processMove(aBoard(), DEFAULT_BOARD_PITS_LENGTH - 1));
    }

    @Test
    void testProcessMoveInvalidPitWithNoStones() {
        assertThrows(IllegalArgumentException.class, () -> kalahaGameEngine.processMove(singleMoveVictoryBoard(), 1));
    }

    @Test
    void testVictoryConditionsCheck() {
        Board board = singleMoveVictoryBoard();
        kalahaGameEngine.processMove(board, 5);
        assertEquals("1-0.0.0.0.0.0.1.-6.6.6.6.6.6.0.", encodeBoardState(board));
        assertEquals(GameState.SOUTH_WIN, board.getState());
    }

}