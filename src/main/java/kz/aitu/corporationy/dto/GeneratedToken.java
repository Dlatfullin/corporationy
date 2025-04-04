package kz.aitu.corporationy.dto;

import java.time.LocalDateTime;

public record GeneratedToken(String token,
                             LocalDateTime expiredAt) {
}