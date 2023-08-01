package dev.ivsan.bolassessment.model;

import lombok.Data;
import lombok.NonNull;

import java.util.List;
import java.util.UUID;

import static dev.ivsan.bolassessment.utils.BoardUtils.initialPitsSetup;

@Data
public class Board {
    @NonNull
    private Player northPlayer;
    @NonNull
    private List<Pit> northPits = initialPitsSetup();

    @NonNull
    private Player southPlayer;
    @NonNull
    private List<Pit> southPits = initialPitsSetup();

    private boolean isNorthTurn;

    @NonNull
    private UUID id = UUID.randomUUID();

    public Board(@NonNull Player northPlayer, @NonNull Player southPlayer) {
        this.northPlayer = northPlayer;
        this.southPlayer = southPlayer;
    }
}
