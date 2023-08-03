package dev.ivsan.bolassessment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.util.UUID;

import static dev.ivsan.bolassessment.utils.MaskingUtils.maskLine;

@Data
public class PlayerLoginResponseDTO {

    @Schema(description = "Selected nickname")
    private String nickname;

    @ToString.Exclude
    @Schema(description = "Api secret that is required as a header for all other requests")
    private String apiSecret;

    @Schema(description = "Error details in case of unsuccessful request")
    private String error;

    @ToString.Include
    private String maskedApiSecret() {
        return maskLine(apiSecret);
    }

    public PlayerLoginResponseDTO(String nickname, UUID id, String apiSecret) {
        this.nickname = nickname;
        this.apiSecret = String.format("%s-%s", id.toString(), apiSecret);
    }

    public PlayerLoginResponseDTO(String error) {
        this.error = error;
    }
}
