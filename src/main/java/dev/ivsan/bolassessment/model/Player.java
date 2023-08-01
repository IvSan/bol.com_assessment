package dev.ivsan.bolassessment.model;

import lombok.Data;
import lombok.NonNull;

import java.util.UUID;

@Data
public class Player {
    private String nickname;

    @NonNull
    private UUID id = UUID.randomUUID();

    public Player(String nickname) {
        this.nickname = nickname;
    }
}
