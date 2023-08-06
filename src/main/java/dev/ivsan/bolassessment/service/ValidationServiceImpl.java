package dev.ivsan.bolassessment.service;

import dev.ivsan.bolassessment.dto.GetBoardRequestDTO;
import dev.ivsan.bolassessment.dto.ListBoardsRequestDTO;
import dev.ivsan.bolassessment.dto.PlayerEnrollRequestDTO;
import dev.ivsan.bolassessment.dto.PlayerLoginRequestDTO;
import dev.ivsan.bolassessment.dto.SubmitMoveRequestDTO;
import dev.ivsan.bolassessment.model.Board;
import dev.ivsan.bolassessment.model.Pit;
import io.github.resilience4j.core.functions.Either;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static dev.ivsan.bolassessment.utils.BoardUtils.listAvailableMoveIndexes;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Service
public class ValidationServiceImpl implements ValidationService {

    @Autowired
    DataManager dataManager;

    private static final int API_SECRET_LENGTH = 53;
    private static final int UUID_LENGTH = 36;
    private static final String UUID_REGEX = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
    private static final String INVALID_SECRET_ERROR = "Invalid api secret, please login again and use the obtained " +
            "api secret for every other request";

    @Override
    public Either<Pair<HttpStatus, String>, PlayerLoginRequestDTO> validateLoginRequest(PlayerLoginRequestDTO request) {
        if (request.getNickname().matches("^[a-zA-Z0-9]+$")) {
            return Either.right(request);
        }
        return Either.left(Pair.of(BAD_REQUEST, "Invalid nickname, please use only english letters and numbers"));
    }

    @Override
    public Either<Pair<HttpStatus, String>, PlayerEnrollRequestDTO> validateEnrollRequest(PlayerEnrollRequestDTO request) {
        return validateSecretAndReturnPlayerId(request.getApiSecret()).map(id -> request);
    }

    @Override
    public Either<Pair<HttpStatus, String>, ListBoardsRequestDTO> validateGetBoardsRequest(ListBoardsRequestDTO request) {
        return validateSecretAndReturnPlayerId(request.getApiSecret()).map(id -> request);
    }

    @Override
    public Either<Pair<HttpStatus, String>, GetBoardRequestDTO> validateGetBoardRequest(
            GetBoardRequestDTO request,
            UUID boardId
    ) {
        Either<Pair<HttpStatus, String>, UUID> errorOrAuthenticatedPlayerId =
                validateSecretAndReturnPlayerId(request.getApiSecret());
        if (errorOrAuthenticatedPlayerId.isLeft()) return Either.left(errorOrAuthenticatedPlayerId.getLeft());

        Either<Pair<HttpStatus, String>, UUID> errorOrAuthorizedBoardId =
                validateIfPlayerOnBoardAndReturnBoardId(errorOrAuthenticatedPlayerId.get(), boardId);
        if (errorOrAuthorizedBoardId.isLeft()) return Either.left(errorOrAuthorizedBoardId.getLeft());

        return Either.right(request);
    }

    @Override
    public Either<Pair<HttpStatus, String>, SubmitMoveRequestDTO> validateSubmitMoveRequest(
            SubmitMoveRequestDTO request,
            UUID boardId
    ) {
        Either<Pair<HttpStatus, String>, UUID> errorOrAuthenticatedPlayerId =
                validateSecretAndReturnPlayerId(request.getApiSecret());
        if (errorOrAuthenticatedPlayerId.isLeft()) return Either.left(errorOrAuthenticatedPlayerId.getLeft());

        Either<Pair<HttpStatus, String>, UUID> errorOrAuthorizedBoardId =
                validateIfPlayerOnBoardAndReturnBoardId(errorOrAuthenticatedPlayerId.get(), boardId);
        if (errorOrAuthorizedBoardId.isLeft()) return Either.left(errorOrAuthorizedBoardId.getLeft());

        Board board = dataManager.findBoardById(errorOrAuthorizedBoardId.get()).orElseThrow();
        if (!errorOrAuthenticatedPlayerId.get().equals(
                board.isNorthTurn() ? board.getNorthPlayer().getId() : board.getSouthPlayer().getId()
        )) {
            return Either.left(Pair.of(BAD_REQUEST, "It's not your move, please wait for opponent's move"));
        }

        List<Pit> pits = board.getNorthPlayer().getId().equals(errorOrAuthenticatedPlayerId.get()) ?
                board.getNorthPits() : board.getSouthPits();
        if (!listAvailableMoveIndexes(pits).contains(request.getMove())) {
            return Either.left(Pair.of(BAD_REQUEST, "Move is not valid"));
        }

        return Either.right(request);
    }

    private Either<Pair<HttpStatus, String>, UUID> validateSecretAndReturnPlayerId(String apiSecret) {
        if (apiSecret == null || apiSecret.length() != API_SECRET_LENGTH) {
            return Either.left(Pair.of(UNAUTHORIZED, INVALID_SECRET_ERROR));
        }
        String userIdRaw = apiSecret.substring(0, UUID_LENGTH);
        String secret = apiSecret.substring(UUID_LENGTH + 1, API_SECRET_LENGTH);
        if (!userIdRaw.matches(UUID_REGEX) || !dataManager.isPlayerSecretValid(UUID.fromString(userIdRaw), secret)) {
            return Either.left(Pair.of(UNAUTHORIZED, INVALID_SECRET_ERROR));
        }
        return Either.right(UUID.fromString(userIdRaw));
    }

    private Either<Pair<HttpStatus, String>, UUID> validateIfPlayerOnBoardAndReturnBoardId(UUID playerId, UUID boardId) {
        if (dataManager.listBoardIdsByPlayerId(playerId).contains(boardId)) {
            return Either.right(boardId);
        } else {
            return Either.left(Pair.of(NOT_FOUND, "Board not found"));
        }
    }
}
