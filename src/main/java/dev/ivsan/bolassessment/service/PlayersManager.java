package dev.ivsan.bolassessment.service;

import dev.ivsan.bolassessment.dto.PlayerEnrollResponseDTO;
import dev.ivsan.bolassessment.dto.PlayerLoginRequestDTO;
import dev.ivsan.bolassessment.dto.PlayerLoginResponseDTO;

import java.util.UUID;

public interface PlayersManager {
    PlayerLoginResponseDTO createPlayer(PlayerLoginRequestDTO request);

    PlayerEnrollResponseDTO enrollInGame(UUID playerId);
}
