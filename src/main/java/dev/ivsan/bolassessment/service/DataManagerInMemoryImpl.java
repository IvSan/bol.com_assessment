package dev.ivsan.bolassessment.service;

import dev.ivsan.bolassessment.model.Board;
import dev.ivsan.bolassessment.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

@Service
public class DataManagerInMemoryImpl implements DataManager {

    @Autowired
    SerializationHelper serializationHelper;

    public DataManagerInMemoryImpl(SerializationHelper serializationHelper) {
        this.serializationHelper = serializationHelper;
    }

    private final HashMap<UUID, String> players = new HashMap<>();
    private final HashMap<UUID, String> apiSecrets = new HashMap<>();
    private final HashMap<UUID, String> boards = new HashMap<>();

    @Override
    public Player savePlayer(Player player) {
        players.put(player.getId(), serializationHelper.serializePlayer(player));
        apiSecrets.putIfAbsent(player.getId(), generateRandomAlphanumeric());
        return player;
    }

    @Override
    public Optional<Player> findPlayerById(UUID id) {
        Optional<String> optional = Optional.ofNullable(players.get(id));
        return optional.map(s -> serializationHelper.deserializePlayer(s));
    }

    @Override
    public Optional<String> findPlayerApiSecretByPlayerId(UUID id) {
        return Optional.ofNullable(apiSecrets.get(id));
    }

    @Override
    public Board saveBoard(Board board) {
        boards.put(board.getId(), serializationHelper.serializeBoard(board));
        return board;
    }

    @Override
    public Optional<Board> findBoardById(UUID id) {
        Optional<String> optional = Optional.ofNullable(boards.get(id));
        return optional.map(s -> serializationHelper.deserializeBoard(s));
    }

    private String generateRandomAlphanumeric() {
        return Long.toHexString(Double.doubleToLongBits(Math.random()));
    }
}
