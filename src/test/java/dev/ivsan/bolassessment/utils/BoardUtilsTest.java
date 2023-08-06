package dev.ivsan.bolassessment.utils;

import dev.ivsan.bolassessment.model.Board;
import dev.ivsan.bolassessment.model.Pit;
import dev.ivsan.bolassessment.model.Player;
import org.junit.jupiter.api.Test;

import java.util.List;

import static dev.ivsan.bolassessment.utils.BoardUtils.BOARD_PITS_LENGTH;
import static dev.ivsan.bolassessment.utils.BoardUtils.PIT_STARTING_STONES;
import static dev.ivsan.bolassessment.utils.BoardUtils.encodeBoardState;
import static dev.ivsan.bolassessment.utils.BoardUtils.getVisualRepresentation;
import static dev.ivsan.bolassessment.utils.BoardUtils.initialPitsSetup;
import static dev.ivsan.bolassessment.utils.BoardUtils.listAvailableMoveIndexes;
import static dev.ivsan.bolassessment.utils.TestDataGenerator.aBoard;
import static dev.ivsan.bolassessment.utils.TestDataGenerator.aPlayer;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BoardUtilsTest {
    @Test
    void shouldInitBoardWithPitsSetup() {
        Board board = Board.builder().northPlayer(aPlayer()).southPlayer(aPlayer()).build();
        checkInitBoardPitsSetupForOneSide(board.getNorthPits());
        checkInitBoardPitsSetupForOneSide(board.getSouthPits());
    }

    private void checkInitBoardPitsSetupForOneSide(List<Pit> pits) {
        assertEquals(BOARD_PITS_LENGTH, pits.size());
        assertEquals(0, pits.get(BOARD_PITS_LENGTH - 1).getStones());
        assertEquals(Pit.PitType.BIG, pits.get(BOARD_PITS_LENGTH - 1).getType());
        assertTrue(pits.stream().limit(BOARD_PITS_LENGTH - 1).allMatch(pit -> PIT_STARTING_STONES == pit.getStones()));
        assertTrue(pits.stream().limit(BOARD_PITS_LENGTH - 1).allMatch(pit -> Pit.PitType.REGULAR == pit.getType()));
    }

    @Test
    void shouldEncodeBoardStateIntoString() {
        Board board = aBoard();
        assertEquals("1-6.6.6.6.6.6.0.-6.6.6.6.6.6.0.", encodeBoardState(board));
    }

    @Test
    void shouldListAvailableMoveIndexes() {
        assertEquals(List.of(0, 1, 2, 3, 4, 5), listAvailableMoveIndexes(initialPitsSetup()));
    }

    @Test
    void shouldGetVisualRepresentationOfBoard() {
        Board board = Board.builder().northPlayer(new Player("Bob")).southPlayer(new Player("Alice")).build();
        assertEquals("""
                North Player: Bob
                [00]( 6)( 6)( 6)( 6)( 6)( 6)
                    ( 6)( 6)( 6)( 6)( 6)( 6)[00]
                South Player: Alice, your turn!
                """, getVisualRepresentation(board));
    }
}