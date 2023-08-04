package dev.ivsan.bolassessment.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LoginResponse {
    private String nickname;
    private String apiSecret;

    @JsonCreator
    public LoginResponse(@JsonProperty("nickname") String nickname, @JsonProperty("apiSecret") String apiSecret) {
        this.nickname = nickname;
        this.apiSecret = apiSecret;
    }
}
