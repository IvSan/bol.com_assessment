package dev.ivsan.bolassessment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pit {
    private int stones = 6;
    @NonNull
    private PitType type = PitType.REGULAR;

    public enum PitType {
        REGULAR, BIG
    }
}


