package dev.ivsan.bolassessment.utils;

import dev.ivsan.bolassessment.model.Board;
import dev.ivsan.bolassessment.model.Player;

public class TestDataGenerator {
    public static Player aPlayer() {
        return new Player("Player nickname");
    }

    public static Board aBoard() {
        return new Board(aPlayer(), aPlayer());
    }
}
