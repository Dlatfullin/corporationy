package kz.aitu.corporationy.dto;

import java.time.LocalDateTime;

public record UserResponse(Long id,
                           String fullName,
                           String username,
                           int followers,
                           LocalDateTime createdAt) {
}