package dev.ivsan.bolassessment.service;

import dev.ivsan.bolassessment.model.Board;
import dev.ivsan.bolassessment.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static java.util.Collections.emptyList;

@Service
public class DataManagerInMemoryImpl implements DataManager {

    @Autowired
    SerializationHelper serializationHelper;

    public DataManagerInMemoryImpl(SerializationHelper serializationHelper) {
        this.serializationHelper = serializationHelper;
    }

    private final Map<UUID, String> players = new HashMap<>();
    private final Map<UUID, String> secrets = new HashMap<>();
    private final Map<UUID, String> boards = new HashMap<>();
    private final Map<UUID, List<UUID>> playerGamesMap = new HashMap<>();

    @Override
    public Player savePlayer(Player player) {
        players.put(player.getId(), serializationHelper.serializePlayer(player));
        secrets.putIfAbsent(player.getId(), generateRandomAlphanumeric());
        return player;
    }

    @Override
    public Optional<Player> findPlayerById(UUID id) {
        Optional<String> optional = Optional.ofNullable(players.get(id));
        return optional.map(s -> serializationHelper.deserializePlayer(s));
    }

    @Override
    public Optional<String> findPlayerSecretByPlayerId(UUID id) {
        return Optional.ofNullable(secrets.get(id));
    }

    @Override
    public Boolean isPlayerSecretValid(UUID playerId, String secret) {
        return secrets.containsKey(playerId) && secrets.get(playerId).equals(secret);
    }

    @Override
    public Board saveBoard(Board board) {
        boards.put(board.getId(), serializationHelper.serializeBoard(board));
        playerGamesMap.computeIfAbsent(board.getNorthPlayer().getId(), k -> new LinkedList<>()).add(board.getId());
        playerGamesMap.computeIfAbsent(board.getSouthPlayer().getId(), k -> new LinkedList<>()).add(board.getId());
        return board;
    }

    @Override
    public Optional<Board> findBoardById(UUID id) {
        Optional<String> optional = Optional.ofNullable(boards.get(id));
        return optional.map(s -> serializationHelper.deserializeBoard(s));
    }

    @Override
    public List<UUID> listBoardIdsByPlayerId(UUID id) {
        return playerGamesMap.getOrDefault(id, emptyList());
    }

    private String generateRandomAlphanumeric() {
        return Long.toHexString(Double.doubleToLongBits(Math.random()));
    }
}
