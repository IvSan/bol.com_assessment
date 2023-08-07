package dev.ivsan.bolassessment.service;

import dev.ivsan.bolassessment.dto.GetBoardRequestDTO;
import dev.ivsan.bolassessment.dto.ListBoardsRequestDTO;
import dev.ivsan.bolassessment.dto.PlayerEnrollRequestDTO;
import dev.ivsan.bolassessment.dto.PlayerLoginRequestDTO;
import dev.ivsan.bolassessment.dto.SubmitMoveRequestDTO;
import io.github.resilience4j.core.functions.Either;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpStatus;

import java.util.UUID;

/**
 * This service validates DTOs from REST endpoints. Each validation method adheres to a uniform pattern: if the request
 * is valid, it returns an Either right with the original request; if not, it returns an Either left with a recommended
 * response code and the corresponding error message.
 */
public interface ValidationService {
    // TODO Think about refactoring signatures to generic

    Either<Pair<HttpStatus, String>, PlayerLoginRequestDTO> validateLoginRequest(PlayerLoginRequestDTO request);

    Either<Pair<HttpStatus, String>, PlayerEnrollRequestDTO> validateEnrollRequest(PlayerEnrollRequestDTO request);

    Either<Pair<HttpStatus, String>, ListBoardsRequestDTO> validateGetBoardsRequest(ListBoardsRequestDTO request);

    Either<Pair<HttpStatus, String>, GetBoardRequestDTO> validateGetBoardRequest(GetBoardRequestDTO request, UUID boardId);

    Either<Pair<HttpStatus, String>, SubmitMoveRequestDTO> validateSubmitMoveRequest(SubmitMoveRequestDTO request, UUID boardId);
}
