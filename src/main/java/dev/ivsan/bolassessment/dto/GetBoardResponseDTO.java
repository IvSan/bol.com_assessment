package dev.ivsan.bolassessment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetBoardResponseDTO {

    @Schema(description = "Board that was requested")
    private BoardResponseDTO board;

    @Schema(description = "Error details in case of unsuccessful request")
    private String error;

    public GetBoardResponseDTO(BoardResponseDTO board) {
        this.board = board;
    }

    public GetBoardResponseDTO(String error) {
        this.error = error;
    }
}