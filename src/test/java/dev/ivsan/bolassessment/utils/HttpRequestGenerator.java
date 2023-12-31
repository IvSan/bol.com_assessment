package dev.ivsan.bolassessment.utils;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static dev.ivsan.bolassessment.utils.TestConstants.PATH_MOVES;
import static dev.ivsan.bolassessment.utils.TestConstants.URL_BOARDS;
import static dev.ivsan.bolassessment.utils.TestConstants.URL_ENROLL;
import static dev.ivsan.bolassessment.utils.TestConstants.URL_LOGIN;

public class HttpRequestGenerator {
    public static RequestBuilder loginRequest(String nickname) {
        return MockMvcRequestBuilders.post(URL_LOGIN)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nickname\":\"" + nickname + "\"}");
    }

    public static RequestBuilder enrollRequest(String apiSecret) {
        return MockMvcRequestBuilders.post(URL_ENROLL)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"apiSecret\":\"" + apiSecret + "\"}");
    }

    public static RequestBuilder getBoardsRequest(String apiSecret) {
        return MockMvcRequestBuilders.get(URL_BOARDS)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"apiSecret\":\"" + apiSecret + "\",\"includeCompleted\":true,\"includeTextRepresentation\":true}");
    }

    public static RequestBuilder getBoardRequest(String apiSecret, UUID boardId) {
        return MockMvcRequestBuilders.get(URL_BOARDS + "/" + boardId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"apiSecret\":\"" + apiSecret + "\",\"includeTextRepresentation\":true}");
    }

    public static RequestBuilder submitMoveRequest(String apiSecret, UUID boardId, int move) {
        return MockMvcRequestBuilders.post(URL_BOARDS + "/" + boardId + PATH_MOVES)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"apiSecret\":\"" + apiSecret + "\",\"move\":" + move + "}");
    }
}
