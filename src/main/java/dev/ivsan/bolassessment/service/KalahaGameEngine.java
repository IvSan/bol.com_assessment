package dev.ivsan.bolassessment.service;

import dev.ivsan.bolassessment.model.Board;

public interface KalahaGameEngine {

    Board processMove(Board board, int pitIndex);

}
