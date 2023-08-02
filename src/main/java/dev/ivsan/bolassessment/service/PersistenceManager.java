package dev.ivsan.bolassessment.service;

import dev.ivsan.bolassessment.model.Board;
import dev.ivsan.bolassessment.model.Player;

import java.util.Optional;
import java.util.UUID;

public interface PersistenceManager {

    Player savePlayer(Player player);

    Optional<Player> findPlayerById(UUID id);

    Board saveBoard(Board board);

    Optional<Board> findBoardById(UUID id);

}
