package dev.ivsan.bolassessment.service;

import dev.ivsan.bolassessment.dto.PlayerLoginRequestDTO;
import io.github.resilience4j.core.functions.Either;

import java.util.UUID;

public interface ValidationService {
    Either<String, UUID> validateSecretAndReturnPlayerId(String apiSecret);

    Either<String, PlayerLoginRequestDTO> validate(PlayerLoginRequestDTO request);
}
