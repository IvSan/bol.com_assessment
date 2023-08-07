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
public class SubmitMoveRequestDTO {
    @ToString.Exclude
    @Schema(
            description = "Mandatory, please use your secret obtained on login step",
            example = "cd965770-eca7-4931-9fc9-de47c6463683-3fef2f3b8a52f079",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String apiSecret;

    @ToString.Exclude
    @Schema(
            description = "The index of the pit from which you wish to make a move, count on your side from left to right",
            example = "1",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private int move;

    @Schema(
            description = "Determines whether or not to include a text-based representation of the game state",
            example = "false"
    )
    private boolean includeTextRepresentation;

    @ToString.Include
    private String maskedApiSecret() {
        return maskSecret(apiSecret);
    }
}