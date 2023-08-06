package dev.ivsan.bolassessment.dto;

import dev.ivsan.bolassessment.model.Board;
import dev.ivsan.bolassessment.model.GameState;
import dev.ivsan.bolassessment.model.Pit;
import dev.ivsan.bolassessment.model.Player;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static dev.ivsan.bolassessment.utils.BoardUtils.getVisualRepresentation;
import static dev.ivsan.bolassessment.utils.BoardUtils.listAvailableMoveIndexes;

public record BoardResponseDTO(
        @Schema(description = "Board id")
        UUID id,
        @Schema(description = "Your player details")
        Player you,
        @Schema(description = "Your opponent's player details")
        Player opponent,
        @Schema(description = "Indicates if it's your turn or not")
        boolean isYourTurn,
        @Schema(description = "In case it's your turn, here is a list of all available moves")
        List<Integer> availableMoves,
        @Schema(description = "List that describes your half of the board")
        List<Pit> yourPits,
        @Schema(description = "List that describes your opponent's half of the board")
        List<Pit> opponentPits,
        @Schema(description = "Game state at given moment")
        GameState state,
        @Schema(description = "History log of all moves")
        List<Integer> moveLog,
        @Schema(description = "Text representation of the board at given moment, it can provide a quick and easily " +
                "interpretable way of displaying the game state.")
        String textRepresentation
) {
    public static BoardResponseDTO generateBoardResponseDtoForPlayer(
            Board board,
            Player adoptForPlayer,
            boolean includeTextRepresentation
    ) {
        boolean isResponseOwnerNorthPlayer = board.getNorthPlayer().getId().equals(adoptForPlayer.getId());
        boolean isResponseOwnerTurn = isResponseOwnerNorthPlayer ^ !board.isNorthTurn();
        return new BoardResponseDTO(
                board.getId(),
                isResponseOwnerNorthPlayer ? board.getNorthPlayer() : board.getSouthPlayer(),
                isResponseOwnerNorthPlayer ? board.getSouthPlayer() : board.getNorthPlayer(),
                isResponseOwnerTurn,
                isResponseOwnerTurn ?
                        listAvailableMoveIndexes(isResponseOwnerNorthPlayer ? board.getNorthPits() : board.getSouthPits()) :
                        Collections.emptyList(),
                isResponseOwnerNorthPlayer ? board.getNorthPits() : board.getSouthPits(),
                isResponseOwnerNorthPlayer ? board.getSouthPits() : board.getNorthPits(),
                board.getState(),
                board.getMoveLog(),
                includeTextRepresentation ? getVisualRepresentation(board) : null
        );
    }
}
