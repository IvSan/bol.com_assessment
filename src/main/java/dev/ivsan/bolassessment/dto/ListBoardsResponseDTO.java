package dev.ivsan.bolassessment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListBoardsResponseDTO {

    @Schema(description = "Boards that still in play")
    private Set<BoardResponseDTO> ongoing;

    @Schema(description = "Archive of completed boards")
    private Set<BoardResponseDTO> completed;

    @Schema(description = "Error details in case of unsuccessful request")
    private String error;

    public ListBoardsResponseDTO(Set<BoardResponseDTO> ongoing, Set<BoardResponseDTO> completed) {
        this.ongoing = ongoing;
        this.completed = completed;
    }

    public ListBoardsResponseDTO(String error) {
        this.error = error;
    }
}