package dev.ivsan.bolassessment.integration;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static dev.ivsan.bolassessment.utils.HttpRequestGenerator.loginRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserLoginTest {

    @Autowired
    private MockMvc mockMvc;

    private static final int SUCCESSFUL_LOGIN_RESPONSE_LENGTH = 86;

    @Test
    public void shouldLoginSuccessfully() throws Exception {
        String bobLoginResult = mockMvc.perform(loginRequest("Bob"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        assertTrue(bobLoginResult.startsWith("{\"nickname\":\"Bob\",\"apiSecret\":"));
        assertEquals(SUCCESSFUL_LOGIN_RESPONSE_LENGTH, bobLoginResult.length());
    }

    @Test
    public void shouldNotLoginSuccessfullyWithInvalidNickname() throws Exception {
        String bobLoginResult = mockMvc.perform(loginRequest("Bob_"))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();
        assertEquals(
                "{\"error\":\"Invalid nickname, please use only english letters and numbers\"}",
                bobLoginResult
        );
    }

}
