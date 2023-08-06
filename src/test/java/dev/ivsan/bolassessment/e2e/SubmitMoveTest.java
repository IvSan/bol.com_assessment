package dev.ivsan.bolassessment.e2e;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ivsan.bolassessment.dto.BoardResponseDTO;
import dev.ivsan.bolassessment.dto.ListBoardsResponseDTO;
import dev.ivsan.bolassessment.dto.PlayerLoginResponseDTO;
import dev.ivsan.bolassessment.dto.SubmitMoveResponseDTO;
import dev.ivsan.bolassessment.model.GameState;
import dev.ivsan.bolassessment.model.Pit;
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

import static dev.ivsan.bolassessment.e2e.UserEnrollTest.loginAndEnroll;
import static dev.ivsan.bolassessment.utils.ApiSecretUtils.getPlayerIdFromSecret;
import static dev.ivsan.bolassessment.utils.BoardUtils.BOARD_PITS_LENGTH;
import static dev.ivsan.bolassessment.utils.HttpRequestGenerator.getBoardsRequest;
import static dev.ivsan.bolassessment.utils.HttpRequestGenerator.submitMoveRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SubmitMoveTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DataManager dataManager;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void shouldSubmitMove() throws Exception {
        PlayerLoginResponseDTO bob = loginAndEnroll(mockMvc, mapper, "Bob");
        PlayerLoginResponseDTO alice = loginAndEnroll(mockMvc, mapper, "Alice");

        String boardsResponseRaw = mockMvc.perform(getBoardsRequest(bob.getApiSecret()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        ListBoardsResponseDTO boardsResponse = mapper.readValue(boardsResponseRaw, ListBoardsResponseDTO.class);

        BoardResponseDTO actualBoard = boardsResponse.getOngoing().iterator().next();
        boolean isBobSouthPlayer = actualBoard.isYourTurn();
        PlayerLoginResponseDTO southPlayer = isBobSouthPlayer ? bob : alice;
        PlayerLoginResponseDTO northPlayer = isBobSouthPlayer ? alice : bob;

        SubmitMoveResponseDTO actualMoveSubmissionResponse = mapper.readValue(
                mockMvc.perform(submitMoveRequest(southPlayer.getApiSecret(), actualBoard.id(), 1))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString(),
                SubmitMoveResponseDTO.class
        );

        SubmitMoveResponseDTO expectedMoveSubmissionResponse = new SubmitMoveResponseDTO(new BoardResponseDTO(
                actualBoard.id(),
                new Player(southPlayer.getNickname(), getPlayerIdFromSecret(southPlayer.getApiSecret()).toString()),
                new Player(northPlayer.getNickname(), getPlayerIdFromSecret(northPlayer.getApiSecret()).toString()),
                false,
                Collections.emptyList(),
                List.of(
                        new Pit(6, Pit.PitType.REGULAR),
                        new Pit(0, Pit.PitType.REGULAR),
                        new Pit(7, Pit.PitType.REGULAR),
                        new Pit(7, Pit.PitType.REGULAR),
                        new Pit(7, Pit.PitType.REGULAR),
                        new Pit(7, Pit.PitType.REGULAR),
                        new Pit(1, Pit.PitType.BIG)
                ),
                List.of(
                        new Pit(7, Pit.PitType.REGULAR),
                        new Pit(6, Pit.PitType.REGULAR),
                        new Pit(6, Pit.PitType.REGULAR),
                        new Pit(6, Pit.PitType.REGULAR),
                        new Pit(6, Pit.PitType.REGULAR),
                        new Pit(6, Pit.PitType.REGULAR),
                        new Pit(0, Pit.PitType.BIG)
                ),
                GameState.IN_PROGRESS,
                List.of(1),
                null
        ));

        assertEquals(expectedMoveSubmissionResponse, actualMoveSubmissionResponse);
    }

    @Test
    public void shouldNotSubmitMoveWhileOpponentTurn() throws Exception {
        PlayerLoginResponseDTO bob = loginAndEnroll(mockMvc, mapper, "Bob");
        PlayerLoginResponseDTO alice = loginAndEnroll(mockMvc, mapper, "Alice");

        String boardsResponseRaw = mockMvc.perform(getBoardsRequest(bob.getApiSecret()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        ListBoardsResponseDTO boardsResponse = mapper.readValue(boardsResponseRaw, ListBoardsResponseDTO.class);

        BoardResponseDTO actualBoard = boardsResponse.getOngoing().iterator().next();
        boolean isBobSouthPlayer = actualBoard.isYourTurn();
        PlayerLoginResponseDTO southPlayer = isBobSouthPlayer ? bob : alice;

        mockMvc.perform(submitMoveRequest(southPlayer.getApiSecret(), actualBoard.id(), 1))
                .andExpect(status().isOk()).andReturn();
        mockMvc.perform(submitMoveRequest(southPlayer.getApiSecret(), actualBoard.id(), 1))
                .andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    public void shouldNotSubmitMoveWithInvalidIndex() throws Exception {
        PlayerLoginResponseDTO bob = loginAndEnroll(mockMvc, mapper, "Bob");
        PlayerLoginResponseDTO alice = loginAndEnroll(mockMvc, mapper, "Alice");

        String boardsResponseRaw = mockMvc.perform(getBoardsRequest(bob.getApiSecret()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        ListBoardsResponseDTO boardsResponse = mapper.readValue(boardsResponseRaw, ListBoardsResponseDTO.class);

        BoardResponseDTO actualBoard = boardsResponse.getOngoing().iterator().next();
        boolean isBobSouthPlayer = actualBoard.isYourTurn();
        PlayerLoginResponseDTO southPlayer = isBobSouthPlayer ? bob : alice;

        mockMvc.perform(submitMoveRequest(southPlayer.getApiSecret(), actualBoard.id(), BOARD_PITS_LENGTH + 1))
                .andExpect(status().isBadRequest()).andReturn();
    }
}
