package dev.ivsan.bolassessment.e2e;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ivsan.bolassessment.dto.PlayerLoginResponseDTO;
import dev.ivsan.bolassessment.service.DataManager;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static dev.ivsan.bolassessment.service.ValidationServiceImpl.INVALID_SECRET_ERROR;
import static dev.ivsan.bolassessment.utils.ApiSecretUtils.getPlayerIdFromSecret;
import static dev.ivsan.bolassessment.utils.HttpRequestGenerator.enrollRequest;
import static dev.ivsan.bolassessment.utils.HttpRequestGenerator.loginRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserEnrollTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private DataManager dataManager;

    @Test
    public void shouldEnrollSuccessfully() throws Exception {
        String loginRaw = mockMvc.perform(loginRequest("Bob"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        PlayerLoginResponseDTO login = mapper.readValue(loginRaw, PlayerLoginResponseDTO.class);

        mockMvc.perform(enrollRequest(login.getApiSecret())).andExpect(status().isOk()).andReturn();
    }

    @Test
    public void shouldEnrollOnlyOnce() throws Exception {
        String loginRaw = mockMvc.perform(loginRequest("Bob"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        PlayerLoginResponseDTO login = mapper.readValue(loginRaw, PlayerLoginResponseDTO.class);

        // enroll twice
        mockMvc.perform(enrollRequest(login.getApiSecret())).andExpect(status().isOk()).andReturn();
        mockMvc.perform(enrollRequest(login.getApiSecret())).andExpect(status().isOk()).andReturn();

        // game hasn't started
        UUID bobId = getPlayerIdFromSecret(login.getApiSecret());
        assertTrue(dataManager.listBoardIdsByPlayerId(bobId).isEmpty());
    }

    @Test
    public void shouldStartGameForTwoEnrolledPlayers() throws Exception {
        PlayerLoginResponseDTO bob = loginAndEnroll(mockMvc, mapper, "Bob");
        PlayerLoginResponseDTO alice = loginAndEnroll(mockMvc, mapper, "Alice");

        UUID bobId = getPlayerIdFromSecret(bob.getApiSecret());
        assertEquals(1, dataManager.listBoardIdsByPlayerId(bobId).size());

        UUID aliceId = getPlayerIdFromSecret(alice.getApiSecret());
        assertEquals(1, dataManager.listBoardIdsByPlayerId(aliceId).size());
    }

    @Test
    public void shouldNotEnrollWithInvalidApiSecret() throws Exception {
        String bobEnrollResult = mockMvc.perform(enrollRequest("invalid"))
                .andExpect(status().isUnauthorized())
                .andReturn().getResponse().getContentAsString();
        assertEquals(
                "{\"error\":\"" + INVALID_SECRET_ERROR + "\"}",
                bobEnrollResult
        );
    }

    public static PlayerLoginResponseDTO loginAndEnroll(MockMvc mockMvc, ObjectMapper mapper, String nickname) throws Exception {
        String loginRaw = mockMvc.perform(loginRequest(nickname))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        PlayerLoginResponseDTO login = mapper.readValue(loginRaw, PlayerLoginResponseDTO.class);
        mockMvc.perform(enrollRequest(login.getApiSecret())).andExpect(status().isOk()).andReturn();
        return login;
    }
}
