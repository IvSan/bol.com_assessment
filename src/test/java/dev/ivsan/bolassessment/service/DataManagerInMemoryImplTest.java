package dev.ivsan.bolassessment.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ivsan.bolassessment.model.Board;
import dev.ivsan.bolassessment.model.Player;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static dev.ivsan.bolassessment.utils.TestDataGenerator.aBoardWithSomeMoves;
import static dev.ivsan.bolassessment.utils.TestDataGenerator.aPlayer;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DataManagerInMemoryImplTest {

    private final DataManager dataManager = new DataManagerInMemoryImpl(
            new SerializationHelperImpl(new ObjectMapper())
    );

    @Test
    void shouldSavePlayerInMemory() {
        Player player = aPlayer();
        dataManager.savePlayer(player);
        Player searchResult = dataManager.findPlayerById(player.getId()).orElseThrow();
        assertEquals(player, searchResult);
    }

    @Test
    void shouldNotFindNonExistingPlayer() {
        assertTrue(dataManager.findPlayerById(UUID.randomUUID()).isEmpty());
    }

    @Test
    void shouldFindPlayerSecret() {
        Player player = aPlayer();
        dataManager.savePlayer(player);
        String secret = dataManager.findPlayerSecretByPlayerId(player.getId()).orElseThrow();
        assertTrue(secret.length() > 0);
    }

    @Test
    void shouldNotFindPlayerSecretForNonExistingPlayer() {
        assertTrue(dataManager.findPlayerSecretByPlayerId(UUID.randomUUID()).isEmpty());
    }

    @Test
    void shouldValidatePlayerSecret() {
        Player player = aPlayer();
        dataManager.savePlayer(player);
        String secret = dataManager.findPlayerSecretByPlayerId(player.getId()).orElseThrow();
        assertTrue(dataManager.isPlayerSecretValid(player.getId(), secret));
    }

    @Test
    void shouldNotValidatePlayerSecret() {
        Player player = aPlayer();
        dataManager.savePlayer(player);
        assertFalse(dataManager.isPlayerSecretValid(player.getId(), "invalid secret"));
    }

    @Test
    void shouldSaveBoardInMemory() {
        Board board = aBoardWithSomeMoves();
        dataManager.saveBoard(board);
        Board searchResult = dataManager.findBoardById(board.getId()).orElseThrow();
        assertEquals(board, searchResult);
    }

    @Test
    void shouldNotFindNonExistingBoard() {
        assertTrue(dataManager.findBoardById(UUID.randomUUID()).isEmpty());
    }

    @Test
    void shouldListBoardIdsByPlayerId() {
        Player bob = new Player("Bob");
        Player alice = new Player("Alice");
        Board board = Board.builder()
                .northPlayer(bob)
                .southPlayer(alice)
                .moveLog(new ArrayList<>(List.of(1, 2, 3)))
                .build();
        dataManager.saveBoard(board);
        assertEquals(Set.of(board.getId()), dataManager.listBoardIdsByPlayerId(bob.getId()));
    }

    @Test
    void shouldListNoBoardsForNeverPlayedPlayed() {
        Player bob = aPlayer();
        assertEquals(Collections.emptySet(), dataManager.listBoardIdsByPlayerId(bob.getId()));
    }
}