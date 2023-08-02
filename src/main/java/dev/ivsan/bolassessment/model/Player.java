package dev.ivsan.bolassessment.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.UUID;

@Data
public class Player {
    private String nickname;
    private UUID id = UUID.randomUUID();

    public Player(String nickname) {
        this.nickname = nickname;
    }

    @JsonCreator
    public Player(@JsonProperty("nickname") String nickname, @JsonProperty("id") String id) {
        this.nickname = nickname;
        this.id = UUID.fromString(id);
    }
}
