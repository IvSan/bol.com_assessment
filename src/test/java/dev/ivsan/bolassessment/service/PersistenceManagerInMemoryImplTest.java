package dev.ivsan.bolassessment.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ivsan.bolassessment.model.Board;
import dev.ivsan.bolassessment.model.Player;
import org.junit.jupiter.api.Test;

import static dev.ivsan.bolassessment.utils.TestDataGenerator.aBoardWithSomeMoves;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PersistenceManagerInMemoryImplTest {

    private final PersistenceManager persistenceManager = new PersistenceManagerInMemoryImpl(
            new SerializationHelperImpl(new ObjectMapper())
    );

    @Test
    void shouldSavePlayerInMemory() {
        Player bob = new Player("Bob");
        persistenceManager.savePlayer(bob);
        Player searchResult = persistenceManager.findPlayerById(bob.getId()).orElseThrow();
        assertEquals(bob, searchResult);
    }

    @Test
    void shouldSaveBoardInMemory() {
        Board board = aBoardWithSomeMoves();
        persistenceManager.saveBoard(board);
        Board searchResult = persistenceManager.findBoardById(board.getId()).orElseThrow();
        assertEquals(board, searchResult);
    }

}