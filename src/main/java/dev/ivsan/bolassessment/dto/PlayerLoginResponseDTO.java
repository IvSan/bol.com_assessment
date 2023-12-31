package dev.ivsan.bolassessment.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.util.UUID;

import static dev.ivsan.bolassessment.utils.ApiSecretUtils.maskSecret;
import static dev.ivsan.bolassessment.utils.ApiSecretUtils.mergePlayerIdAndSecret;

@Data
public class PlayerLoginResponseDTO {

    @Schema(description = "Selected nickname")
    private String nickname;

    @ToString.Exclude
    @Schema(description = "Api secret that is required for all other requests")
    private String apiSecret;

    @Schema(description = "Error details in case of unsuccessful request")
    private String error;

    @ToString.Include
    private String maskedApiSecret() {
        return maskSecret(apiSecret);
    }

    public PlayerLoginResponseDTO(String nickname, UUID id, String secret) {
        this.nickname = nickname;
        this.apiSecret = mergePlayerIdAndSecret(id, secret);
    }

    @JsonCreator
    public PlayerLoginResponseDTO(String nickname, String apiSecret, String error) {
        this.nickname = nickname;
        this.apiSecret = apiSecret;
        this.error = error;
    }

    public PlayerLoginResponseDTO(String error) {
        this.error = error;
    }
}
