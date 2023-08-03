package dev.ivsan.bolassessment.service;

import dev.ivsan.bolassessment.dto.PlayerLoginRequestDTO;
import dev.ivsan.bolassessment.dto.PlayerLoginResponseDTO;

public interface PlayersManager {
    PlayerLoginResponseDTO createPlayer(PlayerLoginRequestDTO request);
}
