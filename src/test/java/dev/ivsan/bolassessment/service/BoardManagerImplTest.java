package dev.ivsan.bolassessment.service;

import dev.ivsan.bolassessment.dto.GetBoardRequestDTO;
import dev.ivsan.bolassessment.dto.GetBoardResponseDTO;
import dev.ivsan.bolassessment.dto.ListBoardsRequestDTO;
import dev.ivsan.bolassessment.dto.ListBoardsResponseDTO;
import dev.ivsan.bolassessment.dto.PlayerEnrollRequestDTO;
import dev.ivsan.bolassessment.dto.SubmitMoveRequestDTO;
import dev.ivsan.bolassessment.dto.SubmitMoveResponseDTO;
import dev.ivsan.bolassessment.model.Board;
import dev.ivsan.bolassessment.model.GameState;
import dev.ivsan.bolassessment.model.Player;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static dev.ivsan.bolassessment.dto.BoardResponseDTO.generateBoardResponseDtoForPlayer;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BoardManagerImplTest {

    private final DataManager dataManager = mock(DataManager.class);

    private final BoardManager boardManager = new BoardManagerImpl(dataManager, new KalahaGameEngineImpl());

    @Test
    void shouldEnrollTwoPlayersIntoNewGame() {
        Player bob = new Player("Bob");
        when(dataManager.findPlayerById(bob.getId())).thenReturn(Optional.of(bob));
        PlayerEnrollRequestDTO enrollBobRequest =
                new PlayerEnrollRequestDTO(bob.getId() + "-3fef2f3b8a52f079");

        Player alice = new Player("Alice");
        when(dataManager.findPlayerById(alice.getId())).thenReturn(Optional.of(alice));
        PlayerEnrollRequestDTO enrollAliceRequest =
                new PlayerEnrollRequestDTO(alice.getId() + "-3fd873568760c4e8");

        boardManager.enrollInGame(enrollBobRequest);
        verify(dataManager, times(0)).saveBoard(any());
        boardManager.enrollInGame(enrollAliceRequest);
        verify(dataManager, times(1)).saveBoard(any());
    }

    @Test
    void shouldNotEnrollSinglePlayerTwiceIntoNewGame() {
        Player bob = new Player("Bob");
        when(dataManager.findPlayerById(bob.getId())).thenReturn(Optional.of(bob));
        PlayerEnrollRequestDTO enrollBobRequest =
                new PlayerEnrollRequestDTO(bob.getId() + "-3fef2f3b8a52f079");

        boardManager.enrollInGame(enrollBobRequest);
        verify(dataManager, times(0)).saveBoard(any());
        boardManager.enrollInGame(enrollBobRequest);
        verify(dataManager, times(0)).saveBoard(any());
    }

    @Test
    void shouldListBoards() {
        Player bob = new Player("Bob");

        Board ongoingBoard = Board.builder()
                .northPlayer(bob)
                .southPlayer(new Player("Alice"))
                .build();
        Board completedBoard = Board.builder()
                .northPlayer(new Player("Alice"))
                .southPlayer(bob)
                .state(GameState.SOUTH_WIN)
                .build();

        when(dataManager.findPlayerById(bob.getId())).thenReturn(Optional.of(bob));
        when(dataManager.listBoardIdsByPlayerId(bob.getId())).thenReturn(Set.of(ongoingBoard.getId(), completedBoard.getId()));
        when(dataManager.findBoardById(ongoingBoard.getId())).thenReturn(Optional.of(ongoingBoard));
        when(dataManager.findBoardById(completedBoard.getId())).thenReturn(Optional.of(completedBoard));

        ListBoardsRequestDTO request =
                new ListBoardsRequestDTO(bob.getId() + "-3fef2f3b8a52f079", true, true);
        ListBoardsResponseDTO actualResponse = boardManager.listBoards(request);

        ListBoardsResponseDTO expectedResponse = new ListBoardsResponseDTO(
                Set.of(generateBoardResponseDtoForPlayer(ongoingBoard, bob, true)),
                Set.of(generateBoardResponseDtoForPlayer(completedBoard, bob, true))
        );

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void shouldGetBoard() {
        Player bob = new Player("Bob");
        Board board = Board.builder()
                .northPlayer(bob)
                .southPlayer(new Player("Alice"))
                .build();

        when(dataManager.findPlayerById(bob.getId())).thenReturn(Optional.of(bob));
        when(dataManager.findBoardById(board.getId())).thenReturn(Optional.of(board));

        GetBoardRequestDTO request = new GetBoardRequestDTO(bob.getId() + "-3fef2f3b8a52f079", true);
        GetBoardResponseDTO actualResponse = boardManager.getBoard(request, board.getId());

        GetBoardResponseDTO expectedResponse = new GetBoardResponseDTO(
                generateBoardResponseDtoForPlayer(board, bob, true)
        );

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void shouldSubmitMove() {
        Player bob = new Player("Bob");
        Board board = Board.builder()
                .northPlayer(bob)
                .southPlayer(new Player("Alice"))
                .build();

        when(dataManager.findPlayerById(bob.getId())).thenReturn(Optional.of(bob));
        when(dataManager.findBoardById(board.getId())).thenReturn(Optional.of(board));
        when(dataManager.saveBoard(board)).thenReturn(board);

        SubmitMoveRequestDTO request = new SubmitMoveRequestDTO(bob.getId() + "-3fef2f3b8a52f079", 1, true);
        SubmitMoveResponseDTO actualResponse = boardManager.submitMove(request, board.getId());

        SubmitMoveResponseDTO expectedResponse = new SubmitMoveResponseDTO(
                generateBoardResponseDtoForPlayer(board, bob, true)
        );

        assertEquals(expectedResponse, actualResponse);
        verify(dataManager, times(1)).saveBoard(any());
    }
}