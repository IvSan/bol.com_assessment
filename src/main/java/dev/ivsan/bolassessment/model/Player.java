package dev.ivsan.bolassessment.model;

import lombok.Data;

import java.util.UUID;

@Data
public class Player {
    private String nickname;
    private UUID id = UUID.randomUUID();

    public Player(String nickname) {
        this.nickname = nickname;
    }
}
