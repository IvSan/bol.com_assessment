package dev.ivsan.bolassessment.service;

import dev.ivsan.bolassessment.dto.PlayerLoginRequestDTO;
import io.github.resilience4j.core.functions.Either;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ValidationServiceImpl implements ValidationService {

    @Autowired
    DataManager dataManager;

    private static final int API_SECRET_LENGTH = 53;
    private static final int UUID_LENGTH = 36;
    private static final String UUID_REGEX =
            "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";
    private static final String INVALID_SECRET_ERROR =
            "Invalid api secret header, please login again and use api secret header for every other request";

    @Override
    public Either<String, UUID> validateSecretAndReturnPlayerId(String apiSecret) {
        if (apiSecret == null || apiSecret.length() != API_SECRET_LENGTH) {
            return Either.left(INVALID_SECRET_ERROR);
        }
        String userIdRaw = apiSecret.substring(0, UUID_LENGTH);
        String secret = apiSecret.substring(UUID_LENGTH + 1, API_SECRET_LENGTH);
        if (!userIdRaw.matches(UUID_REGEX) || !dataManager.isPlayerSecretValid(UUID.fromString(userIdRaw), secret)) {
            return Either.left(INVALID_SECRET_ERROR);
        }
        return Either.right(UUID.fromString(userIdRaw));
    }

    @Override
    public Either<String, PlayerLoginRequestDTO> validate(PlayerLoginRequestDTO request) {
        if (request.getNickname().matches("^[a-zA-Z0-9]+$")) {
            return Either.right(request);
        }
        return Either.left("Invalid nickname, please use only english letters and numbers");
    }
}
