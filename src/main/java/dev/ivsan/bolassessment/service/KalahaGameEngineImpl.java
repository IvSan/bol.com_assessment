package dev.ivsan.bolassessment.service;

import dev.ivsan.bolassessment.model.Board;
import dev.ivsan.bolassessment.model.GameState;
import dev.ivsan.bolassessment.model.Pit;
import org.springframework.stereotype.Service;

import java.util.List;

import static dev.ivsan.bolassessment.utils.BoardUtils.BOARD_PITS_LENGTH;

@Service
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

    private void makeMove(Board board, int pickedPitIndex) {
        board.getMoveLog().add(pickedPitIndex);
        List<Pit> ownPits = board.isNorthTurn() ? board.getNorthPits() : board.getSouthPits();
        int stonesToSow = ownPits.get(pickedPitIndex).getStones();
        ownPits.get(pickedPitIndex).setStones(0);

        int pointer = pickedPitIndex + 1;
        while (stonesToSow > 0) {
            boolean isSowingOnOwnSide = pointer / BOARD_PITS_LENGTH % 2 == 0;
            ownPits = isSowingOnOwnSide ^ board.isNorthTurn() ? board.getSouthPits() : board.getNorthPits();
            int ownPitIndex = pointer % BOARD_PITS_LENGTH;
            Pit pointerPit = ownPits.get(ownPitIndex);
            pointer++;
            if (Pit.PitType.BIG == pointerPit.getType() && !isSowingOnOwnSide)
                continue; // Skip opponent's big pit while sowing
            stonesToSow--;
            pointerPit.setStones(pointerPit.getStones() + 1);
            if (stonesToSow == 0 && isSowingOnOwnSide) {
                if (Pit.PitType.BIG == pointerPit.getType()) return; // Turn ended in own big pit, get another turn
                if (Pit.PitType.REGULAR == pointerPit.getType() && pointerPit.getStones() == 1) {
                    // Turn ended in own regular empty pit, capture own stone and opponent's stones to the big pit
                    pointerPit.setStones(0);
                    int oppositePitIndex = BOARD_PITS_LENGTH - 2 - ownPitIndex;
                    List<Pit> opponentPits = board.isNorthTurn() ? board.getSouthPits() : board.getNorthPits();
                    int stonesFromOpponentPit = opponentPits.get(oppositePitIndex).getStones();
                    opponentPits.get(oppositePitIndex).setStones(0);
                    ownPits.get(BOARD_PITS_LENGTH - 1).setStones(
                            ownPits.get(BOARD_PITS_LENGTH - 1).getStones() + stonesFromOpponentPit + 1
                    );
                }
            }

        }

        board.setNorthTurn(!board.isNorthTurn());
    }

    private void checkForVictory(Board board) {
        if (areAllRegularPitsEmpty(board.getNorthPits()) || areAllRegularPitsEmpty(board.getSouthPits())) {
            int northScores = getScore(board.getNorthPits());
            int southScores = getScore(board.getSouthPits());
            if (northScores > southScores) {
                board.setState(GameState.NORTH_WIN);
            } else if (northScores < southScores) {
                board.setState(GameState.SOUTH_WIN);
            } else {
                board.setState(GameState.TIE);
            }
        }
    }

    private boolean areAllRegularPitsEmpty(List<Pit> pits) {
        return pits.stream().filter(pit -> Pit.PitType.REGULAR == pit.getType()).allMatch(pit -> pit.getStones() <= 0);
    }

    private int getScore(List<Pit> pits) {
        return pits.stream().map(Pit::getStones).reduce(0, Integer::sum);
    }
}
