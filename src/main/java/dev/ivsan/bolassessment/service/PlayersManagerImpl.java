package dev.ivsan.bolassessment.service;

import dev.ivsan.bolassessment.dto.LoginRequestDTO;
import dev.ivsan.bolassessment.dto.LoginResponseDTO;
import dev.ivsan.bolassessment.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayersManagerImpl implements PlayersManager {

    @Autowired
    PersistenceManager persistenceManager;

    @Override
    public LoginResponseDTO createPlayer(LoginRequestDTO request) {
        Player player = persistenceManager.savePlayer(new Player(request.getNickname()));
        String apiSecret = persistenceManager.findPlayerApiSecretByPlayerId(player.getId()).orElse(null);
        return new LoginResponseDTO(player.getNickname(), player.getId(), apiSecret);
    }
}
