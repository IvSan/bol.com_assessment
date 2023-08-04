package dev.ivsan.bolassessment.service;

import dev.ivsan.bolassessment.model.Board;
import dev.ivsan.bolassessment.model.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class BoardManagerImpl implements BoardManager {

    @Autowired
    DataManager dataManager;

    private final Random random = new Random();
    private final ConcurrentHashMap<UUID, Player> enrolledPlayers = new ConcurrentHashMap<>();
    private final static Logger LOG = LoggerFactory.getLogger(BoardManagerImpl.class);

    public void enrollPlayer(Player player) {
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
}
