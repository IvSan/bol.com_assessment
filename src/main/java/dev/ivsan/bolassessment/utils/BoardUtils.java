package dev.ivsan.bolassessment.utils;

import dev.ivsan.bolassessment.model.Board;
import dev.ivsan.bolassessment.model.Pit;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class BoardUtils {
    public static Integer DEFAULT_BOARD_PITS_LENGTH = 7;

    public static List<Pit> initialPitsSetup() {
        return Stream.concat(
                Stream.generate(Pit::new).limit(DEFAULT_BOARD_PITS_LENGTH - 1),
                Stream.generate(() -> new Pit(0, Pit.PitType.BIG)).limit(1)
        ).collect(Collectors.toList());
    }

    public static String encodeBoardState(Board board) {
        StringBuilder builder = new StringBuilder();
        builder.append(board.isNorthTurn() ? 0 : 1).append("-");
        for (Pit pit : board.getSouthPits()) {
            builder.append(pit.getStones()).append(".");
        }
        builder.append("-");
        for (Pit pit : board.getNorthPits()) {
            builder.append(pit.getStones()).append(".");
        }
        return builder.toString();
    }

    public static String getVisualRepresentation(Board board) {
        return String.format("North Player: %s%s%n", board.getNorthPlayer().getNickname(),
                board.isNorthTurn() ? ", your turn!" : "") +
                drawBoardSide(board, true) +
                drawBoardSide(board, false) +
                String.format("South Player: %s%s%n", board.getSouthPlayer().getNickname(),
                        board.isNorthTurn() ? "" : ", your turn!");
    }

    private static String drawBoardSide(Board board, boolean drawNorth) {
        StringBuilder builder = new StringBuilder();
        if (!drawNorth) {
            builder.append("    ");
        }
        List<Pit> pits = drawNorth ? board.getNorthPits() : board.getSouthPits();
        for (int i = 0; i < pits.size(); i++) {
            int pointer = drawNorth ? pits.size() - i - 1 : i;
            Pit pit = pits.get(pointer);
            if (Pit.PitType.REGULAR == pit.getType()) {
                builder.append("(").append(pit.getStones()).append(")");
            } else {
                builder.append("[").append(String.format("%02d", pit.getStones())).append("]");
            }
        }
        builder.append(String.format("%n"));
        return builder.toString();
    }
}