package dev.ivsan.bolassessment.dto;

import dev.ivsan.bolassessment.model.Board;
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
    private Set<Board> inProgress;

    @Schema(description = "Archive of completed boards")
    private Set<Board> completed;

    @Schema(description = "Error details in case of unsuccessful request")
    private String error;

    public ListBoardsResponseDTO(Set<Board> inProgress, Set<Board> completed) {
        this.inProgress = inProgress;
        this.completed = completed;
    }

    public ListBoardsResponseDTO(String error) {
        this.error = error;
    }
}