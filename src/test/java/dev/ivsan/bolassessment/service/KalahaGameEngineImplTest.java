package dev.ivsan.bolassessment.service;

import dev.ivsan.bolassessment.model.Board;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static dev.ivsan.bolassessment.utils.BoardUtils.DEFAULT_BOARD_PITS_LENGTH;
import static dev.ivsan.bolassessment.utils.BoardUtils.getVisualRepresentation;
import static dev.ivsan.bolassessment.utils.TestDataGenerator.aBoard;

class KalahaGameEngineImplTest {

    private final KalahaGameEngine kalahaGameEngine = new KalahaGameEngineImpl();

    @Test
    void testProcessMove() {
        System.out.println(getVisualRepresentation(aBoard()));
        Board boardAfterMove = kalahaGameEngine.processMove(aBoard(), 1);
        System.out.println(getVisualRepresentation(boardAfterMove));
    }

    @Test
    void testProcessMoveInvalidPitIndex() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                kalahaGameEngine.processMove(aBoard(), -1));
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                kalahaGameEngine.processMove(aBoard(), DEFAULT_BOARD_PITS_LENGTH));
    }

}