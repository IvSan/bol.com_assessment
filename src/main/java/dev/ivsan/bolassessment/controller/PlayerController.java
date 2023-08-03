package dev.ivsan.bolassessment.controller;

import dev.ivsan.bolassessment.dto.PlayerLoginRequestDTO;
import dev.ivsan.bolassessment.dto.PlayerLoginResponseDTO;
import dev.ivsan.bolassessment.service.PlayersManager;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/v1")
public class PlayerController {

    @Autowired
    PlayersManager playersManager;

    private final static Logger LOG = LoggerFactory.getLogger(PlayerController.class);

    @PostMapping("/login")
    @Operation(summary = "Login with your nickname")
    public ResponseEntity<PlayerLoginResponseDTO> playerLogin(@RequestBody PlayerLoginRequestDTO request) {
        LOG.info("New '/login' POST request with the request body: {}", request);
        // TODO validation validationService.validate(request);
        PlayerLoginResponseDTO response = playersManager.createPlayer(request);
        LOG.info("Login request completed successfully: {}", response);
        return new ResponseEntity<>(response, CREATED);
    }

}
