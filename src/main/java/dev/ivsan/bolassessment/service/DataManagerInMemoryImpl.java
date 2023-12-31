package dev.ivsan.bolassessment.service;

import dev.ivsan.bolassessment.model.Board;
import dev.ivsan.bolassessment.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static java.util.Collections.emptySet;

@Service
public class DataManagerInMemoryImpl implements DataManager {

    @Autowired
    private final SerializationHelper serializationHelper;

    public DataManagerInMemoryImpl(SerializationHelper serializationHelper) {
        this.serializationHelper = serializationHelper;
    }

    private final Map<UUID, String> players = new HashMap<>();
    private final Map<UUID, String> secrets = new HashMap<>();
    private final Map<UUID, String> boards = new HashMap<>();
    private final Map<UUID, Set<UUID>> playerGamesMap = new HashMap<>();

    @Override
    public Player savePlayer(Player player) {
        players.put(player.getId(), serializationHelper.serializePlayer(player));
        secrets.putIfAbsent(player.getId(), generateRandomAlphanumeric());
        return player;
    }

    @Override
    public Optional<Player> findPlayerById(UUID playerId) {
        Optional<String> optional = Optional.ofNullable(players.get(playerId));
        return optional.map(serializationHelper::deserializePlayer);
    }

    @Override
    public Optional<String> findPlayerSecretByPlayerId(UUID playerId) {
        return Optional.ofNullable(secrets.get(playerId));
    }

    @Override
    public Boolean isPlayerSecretValid(UUID playerId, String secret) {
        return secrets.containsKey(playerId) && secrets.get(playerId).equals(secret);
    }

    @Override
    public Board saveBoard(Board board) {
        boards.put(board.getId(), serializationHelper.serializeBoard(board));
        playerGamesMap.computeIfAbsent(board.getNorthPlayer().getId(), k -> new HashSet<>()).add(board.getId());
        playerGamesMap.computeIfAbsent(board.getSouthPlayer().getId(), k -> new HashSet<>()).add(board.getId());
        return board;
    }

    @Override
    public Optional<Board> findBoardById(UUID boardId) {
        Optional<String> optional = Optional.ofNullable(boards.get(boardId));
        return optional.map(serializationHelper::deserializeBoard);
    }

    @Override
    public Set<UUID> listBoardIdsByPlayerId(UUID playerId) {
        return playerGamesMap.getOrDefault(playerId, emptySet());
    }

    private String generateRandomAlphanumeric() {
        return Long.toHexString(Double.doubleToLongBits(Math.random()));
    }
}
