package dev.ivsan.bolassessment.service;

import dev.ivsan.bolassessment.model.Board;
import dev.ivsan.bolassessment.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

@Service
public class PersistenceManagerInMemoryImpl implements PersistenceManager {

    @Autowired
    SerializationHelper serializationHelper;

    public PersistenceManagerInMemoryImpl(SerializationHelper serializationHelper) {
        this.serializationHelper = serializationHelper;
    }

    private final HashMap<UUID, String> players = new HashMap<>();
    private final HashMap<UUID, String> boards = new HashMap<>();

    @Override
    public Player savePlayer(Player player) {
        players.put(player.getId(), serializationHelper.serializePlayer(player));
        return player;
    }

    @Override
    public Optional<Player> findPlayerById(UUID id) {
        Optional<String> optional = Optional.ofNullable(players.get(id));
        return optional.map(s -> serializationHelper.deserializePlayer(s));
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
}
