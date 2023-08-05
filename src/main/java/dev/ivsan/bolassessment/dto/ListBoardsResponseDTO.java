package dev.ivsan.bolassessment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListBoardsResponseDTO {

    @Schema(description = "Boards that still in play")
    private List<BoardResponseDTO> ongoing;

    @Schema(description = "Archive of completed boards")
    private List<BoardResponseDTO> completed;

    @Schema(description = "Error details in case of unsuccessful request")
    private String error;

    public ListBoardsResponseDTO(List<BoardResponseDTO> ongoing, List<BoardResponseDTO> completed) {
        this.ongoing = ongoing;
        this.completed = completed;
    }

    public ListBoardsResponseDTO(String error) {
        this.error = error;
    }
}