package dev.ivsan.bolassessment.service;

import dev.ivsan.bolassessment.dto.BoardResponseDTO;
import dev.ivsan.bolassessment.dto.GetBoardRequestDTO;
import dev.ivsan.bolassessment.dto.GetBoardResponseDTO;
import dev.ivsan.bolassessment.dto.ListBoardsRequestDTO;
import dev.ivsan.bolassessment.dto.ListBoardsResponseDTO;
import dev.ivsan.bolassessment.dto.PlayerEnrollRequestDTO;
import dev.ivsan.bolassessment.dto.PlayerEnrollResponseDTO;
import dev.ivsan.bolassessment.model.Board;
import dev.ivsan.bolassessment.model.GameState;
import dev.ivsan.bolassessment.model.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static dev.ivsan.bolassessment.dto.BoardResponseDTO.generateBoardResponseDtoForPlayer;
import static dev.ivsan.bolassessment.utils.ApiSecretUtils.getPlayerIdFromSecret;


@Service
public class BoardManagerImpl implements BoardManager {

    @Autowired
    DataManager dataManager;

    private final Random random = new Random();
    private final ConcurrentHashMap<UUID, Player> enrolledPlayers = new ConcurrentHashMap<>();
    private final static Logger LOG = LoggerFactory.getLogger(BoardManagerImpl.class);

    @Override
    public PlayerEnrollResponseDTO enrollInGame(PlayerEnrollRequestDTO request) {
        Player playerToEnroll = dataManager.findPlayerById(getPlayerIdFromSecret(request.getApiSecret())).orElseThrow();
        enrollPlayer(playerToEnroll);
        return new PlayerEnrollResponseDTO();
    }

    private void enrollPlayer(Player player) {
        boolean isPlayerAddedToWaitingList = enrolledPlayers.putIfAbsent(player.getId(), player) == null;
        if (isPlayerAddedToWaitingList) {
            LOG.info("{} has been enrolled into a new game", player);
        }
        startGameIfPossible();
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
        List<Board> boards = dataManager.listBoardIdsByPlayerId(playerToRespond.getId()).stream()
                .map(id -> dataManager.findBoardById(id))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
        List<BoardResponseDTO> ongoingBoards = filterAndMapBoardsToBoardResponseDTO(boards,
                board -> GameState.IN_PROGRESS == board.getState(), playerToRespond, request.isIncludeTextRepresentation());
        List<BoardResponseDTO> completedBoards = request.isIncludeCompleted() ?
                filterAndMapBoardsToBoardResponseDTO(
                        boards, board -> GameState.IN_PROGRESS != board.getState(), playerToRespond, request.isIncludeTextRepresentation()
                ) : null;
        return new ListBoardsResponseDTO(ongoingBoards, completedBoards);
    }

    private List<BoardResponseDTO> filterAndMapBoardsToBoardResponseDTO(
            List<Board> boards,
            Predicate<Board> filter,
            Player playerToRespond,
            boolean includeTextRepresentation
    ) {
        return boards.stream()
                .filter(filter)
                .map(board -> generateBoardResponseDtoForPlayer(board, playerToRespond, includeTextRepresentation))
                .collect(Collectors.toList());
    }

    @Override
    public GetBoardResponseDTO getBoard(GetBoardRequestDTO request, UUID boardId) {
        Board board = dataManager.findBoardById(boardId).orElseThrow();
        Player playerToRespond = dataManager.findPlayerById(getPlayerIdFromSecret(request.getApiSecret())).orElseThrow();
        return new GetBoardResponseDTO(
                generateBoardResponseDtoForPlayer(board, playerToRespond, request.isIncludeTextRepresentation())
        );
    }
}
