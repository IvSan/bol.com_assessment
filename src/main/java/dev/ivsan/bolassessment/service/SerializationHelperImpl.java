package dev.ivsan.bolassessment.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ivsan.bolassessment.model.Board;
import dev.ivsan.bolassessment.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SerializationHelperImpl implements SerializationHelper {

    @Autowired
    ObjectMapper mapper;

    public SerializationHelperImpl(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public String serializePlayer(Player player) {
        try {
            return mapper.writeValueAsString(player);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error processing JSON", e);
        }
    }

    @Override
    public Player deserializePlayer(String serializedPlayer) {
        try {
            return mapper.readValue(serializedPlayer, Player.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error processing JSON", e);
        }
    }

    @Override
    public String serializeBoard(Board board) {
        try {
            return mapper.writeValueAsString(board);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error processing JSON", e);
        }
    }

    @Override
    public Board deserializeBoard(String serializedBoard) {
        try {
            return mapper.readValue(serializedBoard, Board.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error processing JSON", e);
        }
    }
}
