package dev.ivsan.bolassessment.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import dev.ivsan.bolassessment.model.Board;
import dev.ivsan.bolassessment.model.GameState;
import dev.ivsan.bolassessment.model.Pit;
import dev.ivsan.bolassessment.model.Player;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class BoardDeserializer extends StdDeserializer<Board> {

    public BoardDeserializer() {
        this(null);
    }

    public BoardDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Board deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        JsonNode node = mapper.readTree(jp);

        Player northPlayer = mapper.treeToValue(node.get("northPlayer"), Player.class);
        Player southPlayer = mapper.treeToValue(node.get("southPlayer"), Player.class);

        List<Pit> northPits = mapper.readValue(node.get("northPits").traverse(), new TypeReference<>() {
        });
        List<Pit> southPits = mapper.readValue(node.get("southPits").traverse(), new TypeReference<>() {
        });

        boolean isNorthTurn = node.get("isNorthTurn").asBoolean();

        GameState state = GameState.valueOf(node.get("state").asText().toUpperCase());

        List<Integer> moveLog = mapper.readValue(node.get("moveLog").traverse(), new TypeReference<>() {
        });

        UUID id = UUID.fromString(node.get("id").asText());

        return Board.builder()
                .northPlayer(northPlayer)
                .southPlayer(southPlayer)
                .northPits(northPits)
                .southPits(southPits)
                .isNorthTurn(isNorthTurn)
                .state(state)
                .moveLog(moveLog)
                .id(id)
                .build();
    }
}
