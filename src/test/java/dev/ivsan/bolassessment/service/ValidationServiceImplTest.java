package dev.ivsan.bolassessment.service;

import dev.ivsan.bolassessment.dto.GetBoardRequestDTO;
import dev.ivsan.bolassessment.dto.PlayerEnrollRequestDTO;
import dev.ivsan.bolassessment.dto.PlayerLoginRequestDTO;
import dev.ivsan.bolassessment.dto.SubmitMoveRequestDTO;
import dev.ivsan.bolassessment.model.Board;
import dev.ivsan.bolassessment.model.Pit;
import dev.ivsan.bolassessment.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static dev.ivsan.bolassessment.service.ValidationServiceImpl.INVALID_SECRET_ERROR;
import static dev.ivsan.bolassessment.utils.TestDataGenerator.aBoardWithSomeMoves;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

class ValidationServiceImplTest {

    private final DataManager dataManager = mock(DataManager.class);

    private final ValidationService validationService = new ValidationServiceImpl(dataManager);

    private final String validSecret = "cd965770-eca7-4931-9fc9-de47c6463683-3fef2f3b8a52f079";

    @BeforeEach
    void setUp() {
        reset(dataManager);
        when(dataManager.isPlayerSecretValid(
                UUID.fromString("cd965770-eca7-4931-9fc9-de47c6463683"), "3fef2f3b8a52f079")
        ).thenReturn(true);
    }

    @Test
    void shouldValidatePlayerLoginRequest() {
        assertTrue(validationService.validateLoginRequest(new PlayerLoginRequestDTO("Bob")).isRight());
        assertTrue(validationService.validateLoginRequest(new PlayerLoginRequestDTO("Alice3000")).isRight());
        assertTrue(validationService.validateLoginRequest(new PlayerLoginRequestDTO("Select * from")).isLeft());
        assertTrue(validationService.validateLoginRequest(new PlayerLoginRequestDTO("_")).isLeft());
        assertTrue(validationService.validateLoginRequest(new PlayerLoginRequestDTO("")).isLeft());
    }

    @Test
    void shouldValidatePlayerEnrollRequest() {
        PlayerEnrollRequestDTO request = new PlayerEnrollRequestDTO(validSecret);
        assertTrue(validationService.validateEnrollRequest(request).isRight());
    }

    @Test
    void shouldNotValidatePlayerEnrollRequest() {
        assertEquals(
                INVALID_SECRET_ERROR,
                validationService.validateEnrollRequest(new PlayerEnrollRequestDTO()).getLeft().getRight()
        );
        assertEquals(
                INVALID_SECRET_ERROR,
                validationService.validateEnrollRequest(new PlayerEnrollRequestDTO(
                        "cd965770-eca7-4931-9fc9-de47c6463684-invalid")).getLeft().getRight()
        );
        assertEquals(
                INVALID_SECRET_ERROR,
                validationService.validateEnrollRequest(new PlayerEnrollRequestDTO(
                        "cd965770-eca7-4931-9fc9-de47c64-3683-3fef2f3b8a52f079")).getLeft().getRight()
        );
    }

    @Test
    void shouldValidateGetBoardRequest() {
        Board board = aBoardWithSomeMoves();
        when(dataManager.listBoardIdsByPlayerId(UUID.fromString("cd965770-eca7-4931-9fc9-de47c6463683")))
                .thenReturn(Set.of(board.getId()));
        GetBoardRequestDTO request = new GetBoardRequestDTO(validSecret, false);
        assertTrue(validationService.validateGetBoardRequest(request, board.getId()).isRight());
    }

    @Test
    void shouldNotValidateGetBoardRequestIfBoardDoesNotBelongPlayer() {
        Board board = aBoardWithSomeMoves();
        when(dataManager.listBoardIdsByPlayerId(UUID.fromString("cd965770-eca7-4931-9fc9-de47c6463683")))
                .thenReturn(Set.of(board.getId()));
        GetBoardRequestDTO request = new GetBoardRequestDTO(validSecret, false);
        assertEquals(
                "Board not found",
                validationService.validateGetBoardRequest(request, UUID.randomUUID()).getLeft().getRight()
        );
    }

    @Test
    void shouldValidateMoveSubmissionRequest() {
        Board board = Board.builder()
                .northPlayer(new Player("Bob"))
                .southPlayer(new Player("Alice", "cd965770-eca7-4931-9fc9-de47c6463683"))
                .moveLog(new ArrayList<>(List.of(1, 2, 3)))
                .build();
        when(dataManager.listBoardIdsByPlayerId(UUID.fromString("cd965770-eca7-4931-9fc9-de47c6463683")))
                .thenReturn(Set.of(board.getId()));
        when(dataManager.findBoardById(board.getId())).thenReturn(Optional.of(board));

        SubmitMoveRequestDTO request = new SubmitMoveRequestDTO(validSecret, 1, false);
        assertTrue(validationService.validateSubmitMoveRequest(request, board.getId()).isRight());
    }

    @Test
    void shouldNotValidateMoveSubmissionRequestWhileItIsOpponentTurn() {
        Board board = Board.builder()
                .northPlayer(new Player("Alice", "cd965770-eca7-4931-9fc9-de47c6463683"))
                .southPlayer(new Player("Bob"))
                .moveLog(new ArrayList<>(List.of(1, 2, 3)))
                .build();
        when(dataManager.listBoardIdsByPlayerId(UUID.fromString("cd965770-eca7-4931-9fc9-de47c6463683")))
                .thenReturn(Set.of(board.getId()));
        when(dataManager.findBoardById(board.getId())).thenReturn(Optional.of(board));

        SubmitMoveRequestDTO request = new SubmitMoveRequestDTO(validSecret, 1, false);
        assertEquals(
                "It's not your move, please wait for opponent's move",
                validationService.validateSubmitMoveRequest(request, board.getId()).getLeft().getRight()
        );
    }

    @Test
    void shouldNotValidateMoveSubmissionRequestWhilePitIndexIsNotValid() {
        Board board = Board.builder()
                .northPlayer(new Player("Bob"))
                .southPlayer(new Player("Alice", "cd965770-eca7-4931-9fc9-de47c6463683"))
                .moveLog(new ArrayList<>(List.of(1, 2, 3)))
                .southPits(List.of(
                        new Pit(6, Pit.PitType.REGULAR),
                        new Pit(0, Pit.PitType.REGULAR),
                        new Pit(6, Pit.PitType.REGULAR),
                        new Pit(6, Pit.PitType.REGULAR),
                        new Pit(6, Pit.PitType.REGULAR),
                        new Pit(6, Pit.PitType.REGULAR),
                        new Pit(0, Pit.PitType.BIG)
                ))
                .build();
        when(dataManager.listBoardIdsByPlayerId(UUID.fromString("cd965770-eca7-4931-9fc9-de47c6463683")))
                .thenReturn(Set.of(board.getId()));
        when(dataManager.findBoardById(board.getId())).thenReturn(Optional.of(board));

        SubmitMoveRequestDTO request = new SubmitMoveRequestDTO(validSecret, 1, false);
        assertEquals(
                "Move is not valid",
                validationService.validateSubmitMoveRequest(request, board.getId()).getLeft().getRight()
        );
    }
}