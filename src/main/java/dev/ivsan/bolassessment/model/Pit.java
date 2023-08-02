package dev.ivsan.bolassessment.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import static dev.ivsan.bolassessment.utils.BoardUtils.PIT_STARTING_STONES;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pit {
    @Builder.Default
    private int stones = PIT_STARTING_STONES;
    @NonNull
    @Builder.Default
    private PitType type = PitType.REGULAR;

    public enum PitType {
        REGULAR, BIG
    }
}


