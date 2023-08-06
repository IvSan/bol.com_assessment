package dev.ivsan.bolassessment.service;

import dev.ivsan.bolassessment.dto.BoardResponseDTO;
import dev.ivsan.bolassessment.dto.GetBoardRequestDTO;
import dev.ivsan.bolassessment.dto.GetBoardResponseDTO;
import dev.ivsan.bolassessment.dto.ListBoardsRequestDTO;
import dev.ivsan.bolassessment.dto.ListBoardsResponseDTO;
import dev.ivsan.bolassessment.dto.PlayerEnrollRequestDTO;
import dev.ivsan.bolassessment.dto.PlayerEnrollResponseDTO;
import dev.ivsan.bolassessment.dto.SubmitMoveRequestDTO;
import dev.ivsan.bolassessment.dto.SubmitMoveResponseDTO;
import dev.ivsan.bolassessment.model.Board;
import dev.ivsan.bolassessment.model.GameState;
import dev.ivsan.bolassessment.model.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static dev.ivsan.bolassessment.dto.BoardResponseDTO.generateBoardResponseDtoForPlayer;
import static dev.ivsan.bolassessment.utils.ApiSecretUtils.getPlayerIdFromSecret;


@Service
public class BoardManagerImpl implements BoardManager {

    @Autowired
    DataManager dataManager;

    @Autowired
    KalahaGameEngine kalahaGameEngine;

    private final Random random = new Random();
    private final ConcurrentMap<UUID, Player> enrolledPlayers = new ConcurrentHashMap<>();
    private final ConcurrentMap<UUID, Object> boardsUpdateLocks = new ConcurrentHashMap<>();

    private final static Logger LOG = LoggerFactory.getLogger(BoardManagerImpl.class);

    @Override
    public PlayerEnrollResponseDTO enrollInGame(PlayerEnrollRequestDTO request) {
        Player playerToEnroll = dataManager.findPlayerById(getPlayerIdFromSecret(request.getApiSecret())).orElseThrow();
        boolean isPlayerAddedToWaitingList = enrolledPlayers.putIfAbsent(playerToEnroll.getId(), playerToEnroll) == null;
        if (isPlayerAddedToWaitingList) {
            LOG.info("{} has been enrolled into a new game", playerToEnroll);
        }
        startGameIfPossible();
        return new PlayerEnrollResponseDTO();
    }

    private synchronized void startGameIfPossible() {
        if (enrolledPlayers.size() >= 2) {
            Iterator<Player> iterator = enrolledPlayers.values().iterator();

            Player playerOne = iterator.next();
            enrolledPlayers.remove(playerOne.getId());

            Player playerTwo = iterator.next();
            enrolledPlayers.remove(playerTwo.getId());

            boolean swapPlayers = random.nextBoolean();
            Board board = Board.builder()
                    .northPlayer(swapPlayers ? playerTwo : playerOne)
                    .southPlayer(swapPlayers ? playerOne : playerTwo)
                    .build();
            dataManager.saveBoard(board);
            LOG.info("New game has been started! {}", board);
        }
    }

    @Override
    public ListBoardsResponseDTO listBoards(ListBoardsRequestDTO request) {
        Player playerToRespond = dataManager.findPlayerById(getPlayerIdFromSecret(request.getApiSecret())).orElseThrow();
        Set<Board> boards = dataManager.listBoardIdsByPlayerId(playerToRespond.getId()).stream()
                .map(id -> dataManager.findBoardById(id))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
        Set<BoardResponseDTO> ongoingBoards = filterAndMapBoardsToBoardResponseDTO(boards,
                board -> GameState.IN_PROGRESS == board.getState(), playerToRespond, request.isIncludeTextRepresentation());
        Set<BoardResponseDTO> completedBoards = request.isIncludeCompleted() ?
                filterAndMapBoardsToBoardResponseDTO(
                        boards, board -> GameState.IN_PROGRESS != board.getState(), playerToRespond, request.isIncludeTextRepresentation()
                ) : null;
        return new ListBoardsResponseDTO(ongoingBoards, completedBoards);
    }

    private Set<BoardResponseDTO> filterAndMapBoardsToBoardResponseDTO(
            Set<Board> boards,
            Predicate<Board> filter,
            Player playerToRespond,
            boolean includeTextRepresentation
    ) {
        return boards.stream()
                .filter(filter)
                .map(board -> generateBoardResponseDtoForPlayer(board, playerToRespond, includeTextRepresentation))
                .collect(Collectors.toSet());
    }

    @Override
    public GetBoardResponseDTO getBoard(GetBoardRequestDTO request, UUID boardId) {
        Board board = dataManager.findBoardById(boardId).orElseThrow();
        Player playerToRespond = dataManager.findPlayerById(getPlayerIdFromSecret(request.getApiSecret())).orElseThrow();
        return new GetBoardResponseDTO(
                generateBoardResponseDtoForPlayer(board, playerToRespond, request.isIncludeTextRepresentation())
        );
    }

    @Override
    public SubmitMoveResponseDTO submitMove(SubmitMoveRequestDTO request, UUID boardId) {
        Player playerToRespond = dataManager.findPlayerById(getPlayerIdFromSecret(request.getApiSecret())).orElseThrow();

        Object lock = boardsUpdateLocks.computeIfAbsent(boardId, key -> new Object());
        synchronized (lock) {
            try {
                Board board = dataManager.findBoardById(boardId).orElseThrow();
                board = kalahaGameEngine.processMove(board, request.getMove());
                dataManager.saveBoard(board);
                return new SubmitMoveResponseDTO(
                        generateBoardResponseDtoForPlayer(board, playerToRespond, request.isIncludeTextRepresentation())
                );
            } finally {
                boardsUpdateLocks.remove(boardId, lock);
            }
        }
    }
}
