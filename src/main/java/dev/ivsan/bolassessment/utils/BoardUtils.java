package dev.ivsan.bolassessment.utils;

import dev.ivsan.bolassessment.model.Board;
import dev.ivsan.bolassessment.model.Pit;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


public class BoardUtils {
    public static final Integer BOARD_PITS_LENGTH = 7;
    public static final Integer PIT_STARTING_STONES = 6;

    public static List<Pit> initialPitsSetup() {
        return Stream.concat(
                Stream.generate(Pit::new).limit(BOARD_PITS_LENGTH - 1),
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

    public static List<Integer> listAvailableMoveIndexes(List<Pit> pits) {
        return IntStream.range(0, pits.size())
                .filter(pitIndex -> pits.get(pitIndex).getStones() > 0 && Pit.PitType.REGULAR == pits.get(pitIndex).getType())
                .boxed()
                .collect(Collectors.toList());
    }

    public static String getVisualRepresentation(Board board) {
        return String.format("North Player: %s%s%n", board.getNorthPlayer().getNickname(),
                board.isNorthTurn() ? ", your turn!" : "") +
                getVisualRepresentationForOneSide(board, true) +
                getVisualRepresentationForOneSide(board, false) +
                String.format("South Player: %s%s%n", board.getSouthPlayer().getNickname(),
                        board.isNorthTurn() ? "" : ", your turn!");
    }

    private static String getVisualRepresentationForOneSide(Board board, boolean getNorth) {
        StringBuilder builder = new StringBuilder();
        if (!getNorth) {
            builder.append("    ");
        }
        List<Pit> pits = getNorth ? board.getNorthPits() : board.getSouthPits();
        for (int i = 0; i < pits.size(); i++) {
            int pointer = getNorth ? pits.size() - i - 1 : i;
            Pit pit = pits.get(pointer);
            if (Pit.PitType.REGULAR == pit.getType()) {
                builder.append("(").append(String.format("%1$2s", pit.getStones())).append(")");
            } else {
                builder.append("[").append(String.format("%02d", pit.getStones())).append("]");
            }
        }
        builder.append(String.format("%n"));
        return builder.toString();
    }
}
