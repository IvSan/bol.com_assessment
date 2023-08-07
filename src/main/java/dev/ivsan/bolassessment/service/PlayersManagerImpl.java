package dev.ivsan.bolassessment.service;

import dev.ivsan.bolassessment.dto.PlayerLoginRequestDTO;
import dev.ivsan.bolassessment.dto.PlayerLoginResponseDTO;
import dev.ivsan.bolassessment.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayersManagerImpl implements PlayersManager {

    @Autowired
    private final DataManager dataManager;

    public PlayersManagerImpl(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public PlayerLoginResponseDTO createPlayer(PlayerLoginRequestDTO request) {
        Player player = dataManager.savePlayer(new Player(request.getNickname()));
        String secret = dataManager.findPlayerSecretByPlayerId(player.getId()).orElse(null);
        return new PlayerLoginResponseDTO(player.getNickname(), player.getId(), secret);
    }
}
