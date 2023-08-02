package dev.ivsan.bolassessment.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import dev.ivsan.bolassessment.model.Board;
import dev.ivsan.bolassessment.model.Pit;

import java.io.IOException;

public class BoardSerializer extends StdSerializer<Board> {

    public BoardSerializer() {
        this(null);
    }

    public BoardSerializer(Class<Board> t) {
        super(t);
    }

    @Override
    public void serialize(Board board, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();

        gen.writeObjectField("northPlayer", board.getNorthPlayer());
        gen.writeObjectField("southPlayer", board.getSouthPlayer());

        gen.writeArrayFieldStart("northPits");
        for (Pit pit : board.getNorthPits()) {
            gen.writeObject(pit);
        }
        gen.writeEndArray();

        gen.writeArrayFieldStart("southPits");
        for (Pit pit : board.getSouthPits()) {
            gen.writeObject(pit);
        }
        gen.writeEndArray();

        gen.writeBooleanField("isNorthTurn", board.isNorthTurn());
        gen.writeStringField("state", board.getState().toString());

        gen.writeArrayFieldStart("moveLog");
        for (Integer move : board.getMoveLog()) {
            gen.writeNumber(move);
        }
        gen.writeEndArray();

        gen.writeStringField("id", board.getId().toString());

        gen.writeEndObject();
    }
}
