package dev.ivsan.bolassessment.utils;

public class MaskingUtils {
    public static String TAILING_MASK = "****************";

    public static String maskLine(String line) {
        if (line.length() < TAILING_MASK.length()) {
            return TAILING_MASK;
        } else {
            return line.substring(0, line.length() - TAILING_MASK.length()) + TAILING_MASK;
        }
    }
}
