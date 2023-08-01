package dev.ivsan.bolassessment.service;

import dev.ivsan.bolassessment.model.Board;
import dev.ivsan.bolassessment.model.Pit;

import java.util.List;

public class KalahaGameEngineImpl implements KalahaGameEngine {
    @Override
    public Board processMove(Board board, int pitIndex) {
        validateMove(board, pitIndex);
        makeMove(board, pitIndex);
        // TODO Check for victory
        return board;
    }

    private void validateMove(Board board, int pitIndex) {
        List<Pit> pits = board.isNorthTurn() ? board.getNorthPits() : board.getSouthPits();
        if (pitIndex < 0 || pitIndex >= pits.size() - 1) {
            throw new IllegalArgumentException("Invalid move, pit index cannot be " + pitIndex);
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
}
