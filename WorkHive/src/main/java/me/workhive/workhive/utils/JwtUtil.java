package me.workhive.workhive.utils;


import me.workhive.workhive.domain.entities.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

@Component
public class JwtUtil {

    private static final String ALGORITHM = "HmacSHA256";
    private static final String HEADER = base64Url(
            "{\"alg\":\"HS256\",\"typ\":\"JWT\"}".getBytes(StandardCharsets.UTF_8));

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration-ms}")
    private long expirationMs;

    public String generateToken(User user) {
        long now = System.currentTimeMillis();
        long exp = now + expirationMs;

        String payload = base64Url(buildPayload(user.getEmail(), user.getId(), now, exp)
                .getBytes(StandardCharsets.UTF_8));

        String signingInput = HEADER + "." + payload;
        String signature = sign(signingInput);

        return signingInput + "." + signature;
    }

    public boolean validateToken(String token) {
        try {
            String[] parts = splitToken(token);
            if (parts == null) return false;

            String signingInput = parts[0] + "." + parts[1];
            String expectedSignature = sign(signingInput);
            if (!expectedSignature.equals(parts[2])) return false;

            String payloadJson = decodeBase64Url(parts[1]);
            long exp = extractLongClaim(payloadJson, "exp");
            return System.currentTimeMillis() < exp;

        } catch (Exception e) {
            return false;
        }
    }

    public String extractEmail(String token) {
        String[] parts = splitToken(token);
        if (parts == null) throw new IllegalArgumentException("Invalid token");
        String payloadJson = decodeBase64Url(parts[1]);
        return extractStringClaim(payloadJson, "sub");
    }

    private String buildPayload(String email, UUID userId, long iat, long exp) {
        return "{" +
                "\"sub\":\"" + email + "\"," +
                "\"userId\":\"" + userId + "\"," +
                "\"iat\":" + iat + "," +
                "\"exp\":" + exp +
                "}";
    }

    private String sign(String data) {
        try {
            Mac mac = Mac.getInstance(ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(
                    secret.getBytes(StandardCharsets.UTF_8), ALGORITHM);
            mac.init(keySpec);
            byte[] rawHmac = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return base64Url(rawHmac);
        } catch (Exception e) {
            throw new RuntimeException("Error signing JWT", e);
        }
    }

    private String[] splitToken(String token) {
        if (token == null) return null;
        String[] parts = token.split("\\.");
        return parts.length == 3 ? parts : null;
    }

    private static String base64Url(byte[] bytes) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String decodeBase64Url(String encoded) {
        byte[] bytes = Base64.getUrlDecoder().decode(encoded);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    private String extractStringClaim(String json, String key) {
        String search = "\"" + key + "\":\"";
        int start = json.indexOf(search);
        if (start < 0) throw new IllegalArgumentException("Claim not found: " + key);
        start += search.length();
        int end = json.indexOf("\"", start);
        return json.substring(start, end);
    }

    private long extractLongClaim(String json, String key) {
        String search = "\"" + key + "\":";
        int start = json.indexOf(search);
        if (start < 0) throw new IllegalArgumentException("Claim not found: " + key);
        start += search.length();
        int end = start;
        while (end < json.length() && (Character.isDigit(json.charAt(end)))) {
            end++;
        }
        return Long.parseLong(json.substring(start, end));
    }
}
