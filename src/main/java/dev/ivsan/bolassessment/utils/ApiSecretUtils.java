package dev.ivsan.bolassessment.utils;

import java.util.UUID;

public class ApiSecretUtils {
    public static final String TAILING_MASK = "****************";

    public static String mergePlayerIdAndSecret(UUID playerId, String secret) {
        return String.format("%s-%s", playerId.toString(), secret);
    }

    public static UUID getPlayerIdFromSecret(String line) {
        if (line == null) return null;
        return UUID.fromString(line.substring(0, line.lastIndexOf("-")));
    }

    public static String maskSecret(String line) {
        if (line == null) return null;
        if (line.length() < TAILING_MASK.length()) {
            return TAILING_MASK;
        } else {
            return line.substring(0, line.length() - TAILING_MASK.length()) + TAILING_MASK;
        }
    }
}
