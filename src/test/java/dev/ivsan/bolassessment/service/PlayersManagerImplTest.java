package dev.ivsan.bolassessment.service;

import dev.ivsan.bolassessment.dto.PlayerLoginRequestDTO;
import dev.ivsan.bolassessment.dto.PlayerLoginResponseDTO;
import dev.ivsan.bolassessment.model.Player;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PlayersManagerImplTest {

    private final DataManager dataManager = mock(DataManager.class);

    private final PlayersManager playersManager = new PlayersManagerImpl(dataManager);

    @Test
    void shouldHandlePlayerCreation() {
        String nickname = "Bob";
        Player bob = new Player(nickname);
        String bobSecret = "bobSecret";
        when(dataManager.savePlayer(any())).thenReturn(bob);
        when(dataManager.findPlayerSecretByPlayerId(any())).thenReturn(Optional.of(bobSecret));

        PlayerLoginRequestDTO loginRequest = new PlayerLoginRequestDTO(nickname);

        PlayerLoginResponseDTO expectedResponseDTO = new PlayerLoginResponseDTO(nickname, bob.getId(), bobSecret);
        PlayerLoginResponseDTO actualResponseDTO = playersManager.createPlayer(loginRequest);

        assertEquals(expectedResponseDTO, actualResponseDTO);
    }

}