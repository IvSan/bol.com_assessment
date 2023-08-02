package dev.ivsan.bolassessment.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pit {
    @Builder.Default
    private int stones = 6;
    @NonNull
    @Builder.Default
    private PitType type = PitType.REGULAR;

    public enum PitType {
        REGULAR, BIG
    }
}


