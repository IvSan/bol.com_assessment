package dev.ivsan.bolassessment.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ivsan.bolassessment.model.Board;
import dev.ivsan.bolassessment.model.Player;
import org.junit.jupiter.api.Test;

import static dev.ivsan.bolassessment.utils.TestDataGenerator.aBoardWithSomeMoves;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SerializationHelperImplTest {

    private final SerializationHelper serializationHelper = new SerializationHelperImpl(new ObjectMapper());

    @Test
    void shouldSerializeAndDeserializePlayer() {
        Player originalPlayer = new Player("Winner3000");

        String serializedPlayer = serializationHelper.serializePlayer(originalPlayer);
        Player deserializedPlayer = serializationHelper.deserializePlayer(serializedPlayer);

        assertEquals(originalPlayer, deserializedPlayer);
    }

    @Test
    void shouldSerializeAndDeserializeBoard() {
        Board originalBoard = aBoardWithSomeMoves();

        String serializedBoard = serializationHelper.serializeBoard(originalBoard);
        Board deserializedBoard = serializationHelper.deserializeBoard(serializedBoard);

        assertEquals(originalBoard, deserializedBoard);
    }

}