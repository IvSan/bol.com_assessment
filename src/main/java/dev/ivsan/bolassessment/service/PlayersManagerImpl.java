package dev.ivsan.bolassessment.service;

import dev.ivsan.bolassessment.dto.PlayerEnrollResponseDTO;
import dev.ivsan.bolassessment.dto.PlayerLoginRequestDTO;
import dev.ivsan.bolassessment.dto.PlayerLoginResponseDTO;
import dev.ivsan.bolassessment.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PlayersManagerImpl implements PlayersManager {

    @Autowired
    DataManager dataManager;

    @Override
    public PlayerLoginResponseDTO createPlayer(PlayerLoginRequestDTO request) {
        Player player = dataManager.savePlayer(new Player(request.getNickname()));
        String secret = dataManager.findPlayerSecretByPlayerId(player.getId()).orElse(null);
        return new PlayerLoginResponseDTO(player.getNickname(), player.getId(), secret);
    }

    @Override
    public PlayerEnrollResponseDTO enrollInGame(UUID playerId) {
        Player playerToEnroll = dataManager.findPlayerById(playerId).orElseThrow();
        return new PlayerEnrollResponseDTO();
    }
}
