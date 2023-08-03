package dev.ivsan.bolassessment.service;

import dev.ivsan.bolassessment.dto.LoginRequestDTO;
import dev.ivsan.bolassessment.dto.LoginResponseDTO;

public interface PlayersManager {
    LoginResponseDTO createPlayer(LoginRequestDTO request);
}
