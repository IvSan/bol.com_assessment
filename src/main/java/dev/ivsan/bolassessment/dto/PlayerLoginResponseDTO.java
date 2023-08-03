package dev.ivsan.bolassessment.dto;

import lombok.Data;
import lombok.ToString;

import java.util.UUID;

import static dev.ivsan.bolassessment.utils.MaskingUtils.maskLine;

@Data
public class PlayerLoginResponseDTO {
    private String nickname;
    @ToString.Exclude
    private String apiSecret;

    @ToString.Include
    private String maskedApiSecret() {
        return maskLine(apiSecret);
    }

    public PlayerLoginResponseDTO(String nickname, UUID id, String apiSecret) {
        this.nickname = nickname;
        this.apiSecret = String.format("%s-%s", id.toString(), apiSecret);
    }
}
