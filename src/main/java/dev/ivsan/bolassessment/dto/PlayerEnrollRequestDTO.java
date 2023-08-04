package dev.ivsan.bolassessment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static dev.ivsan.bolassessment.utils.ApiSecretUtils.maskSecret;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerEnrollRequestDTO {
    @ToString.Exclude
    @Schema(
            description = "Use your secret obtained on login step",
            example = "cd965770-eca7-4931-9fc9-de47c6463683-3fef2f3b8a52f079"
    )
    private String apiSecret;

    @ToString.Include
    private String maskedApiSecret() {
        return maskSecret(apiSecret);
    }
}