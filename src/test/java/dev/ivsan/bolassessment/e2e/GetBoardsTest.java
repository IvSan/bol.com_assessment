package dev.ivsan.bolassessment.e2e;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ivsan.bolassessment.dto.BoardResponseDTO;
import dev.ivsan.bolassessment.dto.GetBoardResponseDTO;
import dev.ivsan.bolassessment.dto.ListBoardsResponseDTO;
import dev.ivsan.bolassessment.dto.PlayerLoginResponseDTO;
import dev.ivsan.bolassessment.model.GameState;
import dev.ivsan.bolassessment.model.Player;
import dev.ivsan.bolassessment.service.DataManager;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static dev.ivsan.bolassessment.e2e.UserEnrollTest.loginAndEnroll;
import static dev.ivsan.bolassessment.service.ValidationServiceImpl.INVALID_SECRET_ERROR;
import static dev.ivsan.bolassessment.utils.ApiSecretUtils.getPlayerIdFromSecret;
import static dev.ivsan.bolassessment.utils.BoardUtils.getVisualRepresentation;
import static dev.ivsan.bolassessment.utils.BoardUtils.initialPitsSetup;
import static dev.ivsan.bolassessment.utils.HttpRequestGenerator.getBoardRequest;
import static dev.ivsan.bolassessment.utils.HttpRequestGenerator.getBoardsRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class GetBoardsTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DataManager dataManager;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void shouldListBoardsForPlayer() throws Exception {
        String bobNickname = "Bob";
        String aliceNickname = "Alice";

        PlayerLoginResponseDTO bob = loginAndEnroll(mockMvc, mapper, bobNickname);
        PlayerLoginResponseDTO alice = loginAndEnroll(mockMvc, mapper, aliceNickname);

        String boardsResponseRaw = mockMvc.perform(getBoardsRequest(bob.getApiSecret()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        ListBoardsResponseDTO boardsResponse = mapper.readValue(boardsResponseRaw, ListBoardsResponseDTO.class);

        assertEquals(1, boardsResponse.getOngoing().size());
        assertEquals(0, boardsResponse.getCompleted().size());

        UUID bobPlayerId = getPlayerIdFromSecret(bob.getApiSecret());
        UUID alicePlayerId = getPlayerIdFromSecret(alice.getApiSecret());

        BoardResponseDTO actualBoard = boardsResponse.getOngoing().iterator().next();
        boolean isBobSouthPlayer = actualBoard.isYourTurn();

        BoardResponseDTO expectedBoard = new BoardResponseDTO(
                actualBoard.id(),
                new Player(bobNickname, bobPlayerId.toString()),
                new Player(aliceNickname, alicePlayerId.toString()),
                isBobSouthPlayer,
                isBobSouthPlayer ? List.of(0, 1, 2, 3, 4, 5) : Collections.emptyList(),
                initialPitsSetup(),
                initialPitsSetup(),
                GameState.IN_PROGRESS,
                Collections.emptyList(),
                getVisualRepresentation(dataManager.findBoardById(actualBoard.id()).orElseThrow())
        );

        assertEquals(expectedBoard, actualBoard);
    }

    @Test
    public void shouldGetBoardById() throws Exception {
        String bobNickname = "Bob";
        String aliceNickname = "Alice";

        PlayerLoginResponseDTO bob = loginAndEnroll(mockMvc, mapper, bobNickname);
        PlayerLoginResponseDTO alice = loginAndEnroll(mockMvc, mapper, aliceNickname);

        String boardsResponseRaw = mockMvc.perform(getBoardsRequest(bob.getApiSecret()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        ListBoardsResponseDTO boardsResponse = mapper.readValue(boardsResponseRaw, ListBoardsResponseDTO.class);

        UUID bobPlayerId = getPlayerIdFromSecret(bob.getApiSecret());
        UUID alicePlayerId = getPlayerIdFromSecret(alice.getApiSecret());

        String boardResponseRaw = mockMvc.perform(
                        getBoardRequest(bob.getApiSecret(), boardsResponse.getOngoing().iterator().next().id())
                ).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        GetBoardResponseDTO boardResponse = mapper.readValue(boardResponseRaw, GetBoardResponseDTO.class);

        BoardResponseDTO actualBoard = boardResponse.getBoard();
        boolean isBobSouthPlayer = actualBoard.isYourTurn();

        BoardResponseDTO expectedBoard = new BoardResponseDTO(
                actualBoard.id(),
                new Player(bobNickname, bobPlayerId.toString()),
                new Player(aliceNickname, alicePlayerId.toString()),
                isBobSouthPlayer,
                isBobSouthPlayer ? List.of(0, 1, 2, 3, 4, 5) : Collections.emptyList(),
                initialPitsSetup(),
                initialPitsSetup(),
                GameState.IN_PROGRESS,
                Collections.emptyList(),
                getVisualRepresentation(dataManager.findBoardById(actualBoard.id()).orElseThrow())
        );

        assertEquals(expectedBoard, actualBoard);
    }

    @Test
    public void shouldNotListBoardsForPlayerWithInvalidApiSecret() throws Exception {
        String bobEnrollResult = mockMvc.perform(getBoardsRequest("invalid"))
                .andExpect(status().isUnauthorized())
                .andReturn().getResponse().getContentAsString();
        assertEquals(
                "{\"error\":\"" + INVALID_SECRET_ERROR + "\"}",
                bobEnrollResult
        );
    }

    @Test
    public void shouldNotGetBoardByIdForPlayerWithInvalidApiSecret() throws Exception {
        String bobEnrollResult = mockMvc.perform(getBoardRequest("invalid", UUID.randomUUID()))
                .andExpect(status().isUnauthorized())
                .andReturn().getResponse().getContentAsString();
        assertEquals(
                "{\"error\":\"" + INVALID_SECRET_ERROR + "\"}",
                bobEnrollResult
        );
    }

    @Test
    public void shouldNotGetBoardThatDoesNotExist() throws Exception {
        String bobNickname = "Bob";
        PlayerLoginResponseDTO bob = loginAndEnroll(mockMvc, mapper, bobNickname);

        String getBoardResult = mockMvc.perform(getBoardRequest(bob.getApiSecret(), UUID.randomUUID()))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();
        assertEquals(
                "{\"error\":\"Board not found\"}",
                getBoardResult
        );
    }

    @Test
    public void shouldNotGetBoardThatDoesNotBelongToPlayer() throws Exception {
        PlayerLoginResponseDTO bob = loginAndEnroll(mockMvc, mapper, "Bob");
        PlayerLoginResponseDTO alice = loginAndEnroll(mockMvc, mapper, "Alice");
        PlayerLoginResponseDTO carlos = loginAndEnroll(mockMvc, mapper, "Carlos");

        String bobBoardsResponseRaw = mockMvc.perform(getBoardsRequest(bob.getApiSecret()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        ListBoardsResponseDTO bobBoardsResponse = mapper.readValue(bobBoardsResponseRaw, ListBoardsResponseDTO.class);
        UUID bobAndAliceBoardId = bobBoardsResponse.getOngoing().iterator().next().id();

        mockMvc.perform(getBoardRequest(bob.getApiSecret(), bobAndAliceBoardId)).andExpect(status().isOk()).andReturn();
        mockMvc.perform(getBoardRequest(alice.getApiSecret(), bobAndAliceBoardId)).andExpect(status().isOk()).andReturn();
        String carlosGetBoardResult = mockMvc.perform(getBoardRequest(carlos.getApiSecret(), bobAndAliceBoardId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse().getContentAsString();
        assertEquals(
                "{\"error\":\"Board not found\"}",
                carlosGetBoardResult
        );
    }
}
