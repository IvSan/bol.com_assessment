package dev.ivsan.bolassessment.service;

import dev.ivsan.bolassessment.model.Board;
import dev.ivsan.bolassessment.model.GameState;
import dev.ivsan.bolassessment.model.Pit;

import java.util.List;

public class KalahaGameEngineImpl implements KalahaGameEngine {
    @Override
    public Board processMove(Board board, int pitIndex) {
        validateMove(board, pitIndex);
        makeMove(board, pitIndex);
        checkForVictory(board);
        return board;
    }

    private void validateMove(Board board, int pitIndex) {
        if (GameState.IN_PROGRESS != board.getState()) {
            throw new IllegalStateException("Invalid move, game has been played till the end");
        }

        List<Pit> pits = board.isNorthTurn() ? board.getNorthPits() : board.getSouthPits();
        if (pitIndex < 0 || pitIndex >= pits.size() - 1) {
            throw new IllegalArgumentException("Invalid move, pit index cannot be " + pitIndex);
        }
        if (pits.get(pitIndex).getStones() <= 0) {
            throw new IllegalArgumentException("Invalid move, chosen pit is empty, pit index cannot be" + pitIndex);
        }
    }

    private void makeMove(Board board, int pitIndex) {
        List<Pit> pits = board.isNorthTurn() ? board.getNorthPits() : board.getSouthPits();
        int stonesToSow = pits.get(pitIndex).getStones();
        pits.get(pitIndex).setStones(0);

        int pointer = pitIndex + 1;
        while (stonesToSow > 0) {
            stonesToSow--;
            if (pointer >= pits.size()) {
                board.setNorthTurn(!board.isNorthTurn());
                pits = board.isNorthTurn() ? board.getNorthPits() : board.getSouthPits();
                pointer = 0;
            }
            pits.get(pointer).setStones(pits.get(pointer).getStones() + 1);
            pointer++;
        }
    }

    private void checkForVictory(Board board) {
        if (areAllSmallPitsEmpty(board.getNorthPits()) || areAllSmallPitsEmpty(board.getSouthPits())) {
            int northScores = getScoreFromBigPit(board.getNorthPits());
            int southScores = getScoreFromBigPit(board.getSouthPits());
            if (northScores > southScores) {
                board.setState(GameState.NORTH_WIN);
            } else if (northScores < southScores) {
                board.setState(GameState.SOUTH_WIN);
            } else {
                board.setState(GameState.TIE);
            }
        }
    }

    private boolean areAllSmallPitsEmpty(List<Pit> pits) {
        return pits.stream()
                .filter(pit -> Pit.PitType.REGULAR == pit.getType())
                .allMatch(pit -> pit.getStones() <= 0);
    }

    private int getScoreFromBigPit(List<Pit> pits) {
        return pits.stream()
                .filter(pit -> Pit.PitType.BIG == pit.getType())
                .findAny().orElseThrow(() -> new IllegalStateException("Player has no BIG pit, game state is invalid"))
                .getStones();
    }
}
