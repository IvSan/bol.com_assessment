package dev.ivsan.bolassessment.utils;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

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
                .content("{\"apiSecret\":\"" + apiSecret + "\",\"includeCompleted\":true}");
    }
}
