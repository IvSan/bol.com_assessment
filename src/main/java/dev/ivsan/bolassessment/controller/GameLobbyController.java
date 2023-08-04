package dev.ivsan.bolassessment.controller;

import dev.ivsan.bolassessment.dto.PlayerEnrollResponseDTO;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static dev.ivsan.bolassessment.utils.ApiSecretUtils.maskSecret;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1")
public class GameLobbyController {

    @Autowired
    ValidationService validationService;

    @Autowired
    PlayersManager playersManager;

    private final static Logger LOG = LoggerFactory.getLogger(GameLobbyController.class);

    @PostMapping("/login")
    @Operation(summary = "First step. Login with your nickname.")
    public ResponseEntity<PlayerLoginResponseDTO> playerLogin(@RequestBody PlayerLoginRequestDTO request) {
        LOG.info("New '/login' POST request with the request body: {}", request);
        try {
            Either<String, PlayerLoginRequestDTO> errorOrRequest = validationService.validate(request);
            if (errorOrRequest.isLeft()) {
                PlayerLoginResponseDTO response = new PlayerLoginResponseDTO(errorOrRequest.getLeft());
                LOG.warn("Login request invalid: {}", response);
                return new ResponseEntity<>(response, BAD_REQUEST);
            }
            PlayerLoginResponseDTO response = playersManager.createPlayer(errorOrRequest.get());
            LOG.info("Login request completed successfully: {}", response);
            return new ResponseEntity<>(response, CREATED);
        } catch (Exception ex) {
            LOG.error("Login request unsuccessful, internal error", ex);
            return new ResponseEntity<>(
                    new PlayerLoginResponseDTO("Internal server error, please try again later"),
                    INTERNAL_SERVER_ERROR
            );
        }
    }

    @PostMapping("/enroll")
    @Operation(summary = "Second Step. Register your intention to play a game.")
    public ResponseEntity<PlayerEnrollResponseDTO> enrollInGame(@RequestHeader("apiSecret") String apiSecret) {
        LOG.info("New '/enroll' POST request prom player with secret: {}", maskSecret(apiSecret));
        try {
            Either<String, UUID> errorOrPlayerId = validationService.validateSecretAndReturnPlayerId(apiSecret);
            if (errorOrPlayerId.isLeft()) {
                PlayerEnrollResponseDTO response = new PlayerEnrollResponseDTO(errorOrPlayerId.getLeft());
                LOG.warn("Enroll request invalid: {}", response);
                return new ResponseEntity<>(response, BAD_REQUEST);
            }
            PlayerEnrollResponseDTO response = playersManager.enrollInGame(errorOrPlayerId.get());
            LOG.info("Enroll request completed successfully: {}", response);
            return new ResponseEntity<>(response, OK);
        } catch (Exception ex) {
            LOG.error("Enroll request unsuccessful, internal error", ex);
            return new ResponseEntity<>(
                    new PlayerEnrollResponseDTO("Internal server error, please try again later"),
                    INTERNAL_SERVER_ERROR
            );
        }
    }

    // TODO Add ability to deregister from the game.
}
