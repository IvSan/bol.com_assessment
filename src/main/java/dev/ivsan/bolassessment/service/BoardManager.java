package dev.ivsan.bolassessment.service;

import dev.ivsan.bolassessment.dto.GetBoardRequestDTO;
import dev.ivsan.bolassessment.dto.GetBoardResponseDTO;
import dev.ivsan.bolassessment.dto.ListBoardsRequestDTO;
import dev.ivsan.bolassessment.dto.ListBoardsResponseDTO;
import dev.ivsan.bolassessment.dto.PlayerEnrollRequestDTO;
import dev.ivsan.bolassessment.dto.PlayerEnrollResponseDTO;
import dev.ivsan.bolassessment.dto.SubmitMoveRequestDTO;
import dev.ivsan.bolassessment.dto.SubmitMoveResponseDTO;

import java.util.UUID;

/**
 * Service to start and manage games and submit player's moves.
 */
public interface BoardManager {

    /**
     * Register player's intention to play a game. Multiple enrollments for same player are fine. A new game will start
     * automatically as soon as two different players enroll.
     */
    PlayerEnrollResponseDTO enrollInGame(PlayerEnrollRequestDTO request);

    /**
     * List all boards, that have given player as one of contenders.
     */
    ListBoardsResponseDTO listBoards(ListBoardsRequestDTO request);

    /**
     * Get one particular board by ID. Requester player must be one of requested board's contenders.
     */
    GetBoardResponseDTO getBoard(GetBoardRequestDTO request, UUID boardId);

    /**
     * Submit a player's move for given board.
     */
    SubmitMoveResponseDTO submitMove(SubmitMoveRequestDTO request, UUID boardId);
}
