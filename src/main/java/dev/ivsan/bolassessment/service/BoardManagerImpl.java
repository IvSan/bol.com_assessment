package dev.ivsan.bolassessment.service;

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
import java.util.stream.Collectors;

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
        List<Board> boards = dataManager.listBoardIdsByPlayerId(getPlayerIdFromSecret(request.getApiSecret())).stream()
                .map(id -> dataManager.findBoardById(id))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
        return new ListBoardsResponseDTO(
                boards.stream().filter(b -> GameState.IN_PROGRESS == b.getState()).collect(Collectors.toSet()),
                request.isIncludeCompleted() ?
                        boards.stream().filter(b -> GameState.IN_PROGRESS != b.getState()).collect(Collectors.toSet()) :
                        null
        );
    }

    @Override
    public GetBoardResponseDTO getBoard(UUID boardId) {
        return new GetBoardResponseDTO(dataManager.findBoardById(boardId).orElseThrow());
    }
}
