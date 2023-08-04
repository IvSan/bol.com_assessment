package dev.ivsan.bolassessment.service;

import dev.ivsan.bolassessment.dto.ListBoardsRequestDTO;
import dev.ivsan.bolassessment.dto.PlayerEnrollRequestDTO;
import dev.ivsan.bolassessment.dto.PlayerLoginRequestDTO;
import io.github.resilience4j.core.functions.Either;

public interface ValidationService {
    Either<String, PlayerLoginRequestDTO> validateLoginRequest(PlayerLoginRequestDTO request);

    Either<String, PlayerEnrollRequestDTO> validateEnrollRequest(PlayerEnrollRequestDTO request);

    Either<String, ListBoardsRequestDTO> validateGetBoardsRequest(ListBoardsRequestDTO request);
}
