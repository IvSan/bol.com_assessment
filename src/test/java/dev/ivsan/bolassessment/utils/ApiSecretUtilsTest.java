package dev.ivsan.bolassessment.utils;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static dev.ivsan.bolassessment.utils.ApiSecretUtils.TAILING_MASK;
import static dev.ivsan.bolassessment.utils.ApiSecretUtils.getPlayerIdFromSecret;
import static dev.ivsan.bolassessment.utils.ApiSecretUtils.maskSecret;
import static dev.ivsan.bolassessment.utils.ApiSecretUtils.mergePlayerIdAndSecret;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ApiSecretUtilsTest {

    @Test
    void shouldMergePlayerIdAndSecret() {
        UUID id = UUID.randomUUID();
        assertEquals(mergePlayerIdAndSecret(id, "secret"), id + "-secret");
    }

    @Test
    void shouldGetPlayerIdFromSecret() {
        assertEquals(
                getPlayerIdFromSecret("cd965770-eca7-4931-9fc9-de47c6463683-3fef2f3b8a52f079"),
                UUID.fromString("cd965770-eca7-4931-9fc9-de47c6463683")
        );
    }

    @Test
    void shouldMaskSecret() {
        assertEquals(
                maskSecret("cd965770-eca7-4931-9fc9-de47c6463683-3fef2f3b8a52f079"),
                "cd965770-eca7-4931-9fc9-de47c6463683-" + TAILING_MASK
        );
    }

}