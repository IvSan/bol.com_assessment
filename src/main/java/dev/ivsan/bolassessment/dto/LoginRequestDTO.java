package dev.ivsan.bolassessment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO {
    @Schema(
            description = "Pick your favourite nickname, used to help players identify each other. " +
                    "Please use only english letters and numbers",
            example = "Champion-Bob"
    )
    private String nickname;
}
