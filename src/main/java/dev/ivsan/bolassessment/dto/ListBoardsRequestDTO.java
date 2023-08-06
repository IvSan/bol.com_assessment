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
public class ListBoardsRequestDTO {
    @ToString.Exclude
    @Schema(
            description = "Mandatory, please use your secret obtained on login step",
            example = "cd965770-eca7-4931-9fc9-de47c6463683-3fef2f3b8a52f079"
    )
    private String apiSecret;

    @Schema(
            description = "Flag to include not only boards in play, but also completed boards",
            example = "false"
    )
    private boolean includeCompleted;

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