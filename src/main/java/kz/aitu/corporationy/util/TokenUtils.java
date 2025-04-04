package kz.aitu.corporationy.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Optional;
import java.util.Random;

public final class TokenUtils {

    private static final String HASH_ALGORITHM = "SHA-256";
    private static final String BEARER_TOKEN_BEGINNING = "Bearer ";
    private static final short TOKEN_BYTES_LENGTH = 16;
    private static final Random RANDOM = new Random();

    private TokenUtils() {
        throw new UnsupportedOperationException();
    }

    public static byte[] createPlainTextToken() {
        var randomBytes = new byte[TOKEN_BYTES_LENGTH];
        RANDOM.nextBytes(randomBytes);
        return Base64.getEncoder().encode(randomBytes);
    }

    public static byte[] hash(final String token) {
        return hash(token.getBytes(StandardCharsets.UTF_8));
    }

    @SneakyThrows
    public static byte[] hash(final byte[] token) {
        var digest = MessageDigest.getInstance(HASH_ALGORITHM);
        return digest.digest(token);
    }

    public static Optional<String> extractTokenFromHeaders(final HttpServletRequest request) {
        var authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        return authHeader != null && authHeader.startsWith(BEARER_TOKEN_BEGINNING)
                ? Optional.of(authHeader.substring(7))
                : Optional.empty();
    }
}