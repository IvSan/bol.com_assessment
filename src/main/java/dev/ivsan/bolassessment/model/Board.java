package dev.ivsan.bolassessment.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;
import java.util.UUID;

import static dev.ivsan.bolassessment.utils.BoardUtils.initialPitsSetup;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Board {
    @NonNull
    private Player northPlayer;
    @NonNull
    @Builder.Default
    private List<Pit> northPits = initialPitsSetup();

    @NonNull
    private Player southPlayer;
    @NonNull
    @Builder.Default
    private List<Pit> southPits = initialPitsSetup();

    private boolean isNorthTurn;
    @Builder.Default
    private GameState state = GameState.IN_PROGRESS;

    @NonNull
    @Builder.Default
    private UUID id = UUID.randomUUID();
}
