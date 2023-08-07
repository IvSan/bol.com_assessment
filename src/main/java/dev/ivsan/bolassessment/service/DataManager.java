package dev.ivsan.bolassessment.service;

import dev.ivsan.bolassessment.model.Board;
import dev.ivsan.bolassessment.model.Player;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Service responsible for storing and providing players and games data.
 */
public interface DataManager {

    /**
     * Store the player's status and generate a unique secret for them if none exists. Secrets are required to
     * authenticate all player's API requests except login.
     */
    Player savePlayer(Player player);

    Optional<Player> findPlayerById(UUID playerId);

    Optional<String> findPlayerSecretByPlayerId(UUID playerId);

    Boolean isPlayerSecretValid(UUID playerId, String secret);

    Board saveBoard(Board board);

    Optional<Board> findBoardById(UUID boardId);

    Set<UUID> listBoardIdsByPlayerId(UUID playerId);
}
