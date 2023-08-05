package dev.ivsan.bolassessment.dto;

import dev.ivsan.bolassessment.model.Board;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetBoardResponseDTO {

    @Schema(description = "Board that was requested")
    private Board board;

    @Schema(description = "Error details in case of unsuccessful request")
    private String error;

    public GetBoardResponseDTO(Board board) {
        this.board = board;
    }

    public GetBoardResponseDTO(String error) {
        this.error = error;
    }
}