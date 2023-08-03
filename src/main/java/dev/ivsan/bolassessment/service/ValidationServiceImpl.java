package dev.ivsan.bolassessment.service;

import dev.ivsan.bolassessment.dto.PlayerLoginRequestDTO;
import io.github.resilience4j.core.functions.Either;
import org.springframework.stereotype.Service;

@Service
public class ValidationServiceImpl implements ValidationService {
    @Override
    public Either<String, PlayerLoginRequestDTO> validate(PlayerLoginRequestDTO request) {
        if (request.getNickname().matches("^[a-zA-Z0-9]+$")) {
            return Either.right(request);
        }
        return Either.left("Invalid nickname, please use only english letters and numbers");
    }
}
