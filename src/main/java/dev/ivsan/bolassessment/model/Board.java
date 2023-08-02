package dev.ivsan.bolassessment.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import dev.ivsan.bolassessment.serializer.BoardDeserializer;
import dev.ivsan.bolassessment.serializer.BoardSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static dev.ivsan.bolassessment.utils.BoardUtils.initialPitsSetup;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize(using = BoardSerializer.class)
@JsonDeserialize(using = BoardDeserializer.class)
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
    @NonNull
    @Builder.Default
    private GameState state = GameState.IN_PROGRESS;
    @NonNull
    @Builder.Default
    private List<Integer> moveLog = new ArrayList<>();

    @NonNull
    @Builder.Default
    private UUID id = UUID.randomUUID();
}
