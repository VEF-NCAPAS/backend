package me.workhive.workhive.utils;

import java.text.Normalizer;

public class StringUtils {
    public static String normalize(String input) {
        if (input == null) return "";
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        normalized = normalized.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return normalized.toLowerCase().trim();
    }
}