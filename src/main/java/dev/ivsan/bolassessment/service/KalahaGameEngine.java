package dev.ivsan.bolassessment.service;

import dev.ivsan.bolassessment.model.Board;

/**
 * Service handling all game related logic.
 */
public interface KalahaGameEngine {

    /**
     * Calculate board's state after applying a new move.
     *
     * @param board    Board state before move.
     * @param pitIndex Active player is picking one of the pits on his side to move with, index if this pit counting
     *                 from left to right is the pitIndex.
     * @return Board state after move.
     */
    Board processMove(Board board, int pitIndex);

}
