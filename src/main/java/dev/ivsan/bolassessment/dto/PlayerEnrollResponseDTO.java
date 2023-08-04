package dev.ivsan.bolassessment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.UUID;

@Data
public class PlayerEnrollResponseDTO {
    private State state;

    private UUID boardId;

    @Schema(description = "Error details in case of unsuccessful request")
    private String error;

    public PlayerEnrollResponseDTO(String error) {
        this.error = error;
    }

    public PlayerEnrollResponseDTO() {
        state = State.WAITING_FOR_OPPONENT;
    }

    public enum State {
        WAITING_FOR_OPPONENT, GAME_STARTED
    }
}