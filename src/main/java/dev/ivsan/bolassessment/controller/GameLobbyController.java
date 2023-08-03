package dev.ivsan.bolassessment.controller;

import dev.ivsan.bolassessment.dto.PlayerLoginRequestDTO;
import dev.ivsan.bolassessment.dto.PlayerLoginResponseDTO;
import dev.ivsan.bolassessment.service.PlayersManager;
import dev.ivsan.bolassessment.service.ValidationService;
import io.github.resilience4j.core.functions.Either;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/v1")
public class GameLobbyController {

    @Autowired
    ValidationService validationService;

    @Autowired
    PlayersManager playersManager;

    private final static Logger LOG = LoggerFactory.getLogger(GameLobbyController.class);

    @PostMapping("/login")
    @Operation(summary = "Login with your nickname")
    public ResponseEntity<PlayerLoginResponseDTO> playerLogin(@RequestBody PlayerLoginRequestDTO request) {
        LOG.info("New '/login' POST request with the request body: {}", request);

        Either<String, PlayerLoginRequestDTO> errorOrRequest = validationService.validate(request);
        if (errorOrRequest.isLeft()) {
            PlayerLoginResponseDTO response = new PlayerLoginResponseDTO(errorOrRequest.getLeft());
            LOG.warn("Login request successful: {}", response);
            return new ResponseEntity<>(response, BAD_REQUEST);
        }
        PlayerLoginResponseDTO response = playersManager.createPlayer(errorOrRequest.get());

        LOG.info("Login request completed successfully: {}", response);
        return new ResponseEntity<>(response, CREATED);
    }
}
