package dev.ivsan.bolassessment.service;

import dev.ivsan.bolassessment.dto.PlayerLoginRequestDTO;
import io.github.resilience4j.core.functions.Either;

public interface ValidationService {
    Either<String, PlayerLoginRequestDTO> validate(PlayerLoginRequestDTO request);
}
