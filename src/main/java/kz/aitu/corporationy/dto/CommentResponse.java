package kz.aitu.corporationy.dto;

import java.time.LocalDateTime;

public record CommentResponse(Long id,
                              String content,
                              String author,
                              LocalDateTime createdAt) {
}