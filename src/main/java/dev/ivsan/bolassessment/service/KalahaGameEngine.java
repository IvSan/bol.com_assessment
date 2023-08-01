package dev.ivsan.bolassessment.service;

import dev.ivsan.bolassessment.model.Board;
import org.springframework.stereotype.Service;

@Service
public interface KalahaGameEngine {

    Board processMove(Board board, int pitIndex);

}
