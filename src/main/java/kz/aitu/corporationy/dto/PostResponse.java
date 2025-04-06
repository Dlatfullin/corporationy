package kz.aitu.corporationy.dto;

import java.time.LocalDateTime;

public record PostResponse(Long id,
                           String title,
                           String content,
                           String author,
                           int likes,
                           LocalDateTime createdAt) {
}