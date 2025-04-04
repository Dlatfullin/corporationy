package kz.aitu.corporationy.service.impl;

import jakarta.persistence.EntityNotFoundException;
import kz.aitu.corporationy.dto.GeneratedToken;
import kz.aitu.corporationy.entity.Token;
import kz.aitu.corporationy.repository.TokenRepository;
import kz.aitu.corporationy.repository.UserRepository;
import kz.aitu.corporationy.service.TokenService;
import kz.aitu.corporationy.util.TokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    @Value("${spring.security.token.expire-after}")
    private Duration expireAfter;

    @Override
    public GeneratedToken generate(String username) {
        var plainText = TokenUtils.createPlainTextToken();
        var hashedToken = TokenUtils.hash(plainText);
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User with username %s not found".formatted(username)));
        var expiredAt = LocalDateTime.now().plus(expireAfter);
        tokenRepository.save(new Token(hashedToken, expiredAt, user));
        return new GeneratedToken(new String(plainText, StandardCharsets.UTF_8), expiredAt);
    }
}