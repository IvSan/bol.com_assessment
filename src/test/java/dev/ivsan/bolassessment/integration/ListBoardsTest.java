package dev.ivsan.bolassessment.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ivsan.bolassessment.dto.ListBoardsResponseDTO;
import dev.ivsan.bolassessment.dto.PlayerLoginResponseDTO;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static dev.ivsan.bolassessment.integration.UserEnrollTest.loginAndEnroll;
import static dev.ivsan.bolassessment.utils.HttpRequestGenerator.getBoardsRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ListBoardsTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void shouldListBoardsForPlayer() throws Exception {
        PlayerLoginResponseDTO bob = loginAndEnroll(mockMvc, mapper, "Bob");
        PlayerLoginResponseDTO alice = loginAndEnroll(mockMvc, mapper, "Alice");

        String boardsResponseRaw = mockMvc.perform(getBoardsRequest(bob.getApiSecret()))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        ListBoardsResponseDTO boardsResponse = mapper.readValue(boardsResponseRaw, ListBoardsResponseDTO.class);

        assertEquals(1, boardsResponse.getOngoing().size());
        assertEquals(0, boardsResponse.getCompleted().size());
    }
}
