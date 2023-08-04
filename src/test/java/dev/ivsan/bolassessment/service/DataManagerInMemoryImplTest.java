package dev.ivsan.bolassessment.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ivsan.bolassessment.model.Board;
import org.junit.jupiter.api.Test;

import static dev.ivsan.bolassessment.utils.TestDataGenerator.aBoardWithSomeMoves;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DataManagerInMemoryImplTest {

    private final DataManager dataManager = new DataManagerInMemoryImpl(
            new SerializationHelperImpl(new ObjectMapper())
    );

    @Test
    void shouldSaveBoardInMemory() {
        Board board = aBoardWithSomeMoves();
        dataManager.saveBoard(board);
        Board searchResult = dataManager.findBoardById(board.getId()).orElseThrow();
        assertEquals(board, searchResult);
    }

}