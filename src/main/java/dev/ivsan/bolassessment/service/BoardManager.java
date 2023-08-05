package dev.ivsan.bolassessment.service;

import dev.ivsan.bolassessment.dto.GetBoardRequestDTO;
import dev.ivsan.bolassessment.dto.GetBoardResponseDTO;
import dev.ivsan.bolassessment.dto.ListBoardsRequestDTO;
import dev.ivsan.bolassessment.dto.ListBoardsResponseDTO;
import dev.ivsan.bolassessment.dto.PlayerEnrollRequestDTO;
import dev.ivsan.bolassessment.dto.PlayerEnrollResponseDTO;

import java.util.UUID;

public interface BoardManager {
    PlayerEnrollResponseDTO enrollInGame(PlayerEnrollRequestDTO request);

    ListBoardsResponseDTO listBoards(ListBoardsRequestDTO request);

    GetBoardResponseDTO getBoard(GetBoardRequestDTO request, UUID boardId);
}
