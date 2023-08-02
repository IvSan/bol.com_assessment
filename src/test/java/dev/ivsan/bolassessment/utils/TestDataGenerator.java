package dev.ivsan.bolassessment.utils;

import dev.ivsan.bolassessment.model.Board;
import dev.ivsan.bolassessment.model.Pit;
import dev.ivsan.bolassessment.model.Player;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static dev.ivsan.bolassessment.utils.BoardUtils.BOARD_PITS_LENGTH;

public class TestDataGenerator {
    public static Player aPlayer() {
        return new Player("Player nickname");
    }

    public static Board aBoard() {
        return Board.builder().northPlayer(aPlayer()).southPlayer(aPlayer()).build();
    }

    public static Board singleMoveVictoryBoard() {
        return Board.builder()
                .northPlayer(aPlayer())
                .northPits(
                        Stream.concat(
                                Stream.generate(Pit::new).limit(BOARD_PITS_LENGTH - 1),
                                Stream.generate(() -> new Pit(0, Pit.PitType.BIG)).limit(1)
                        ).collect(Collectors.toList())
                )
                .southPlayer(aPlayer())
                .southPits(List.of(
                        new Pit(0, Pit.PitType.REGULAR),
                        new Pit(0, Pit.PitType.REGULAR),
                        new Pit(0, Pit.PitType.REGULAR),
                        new Pit(0, Pit.PitType.REGULAR),
                        new Pit(0, Pit.PitType.REGULAR),
                        new Pit(1, Pit.PitType.REGULAR),
                        new Pit(0, Pit.PitType.BIG)
                ))
                .build();
    }
}
