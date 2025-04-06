package kz.aitu.corporationy.dto;

import jakarta.validation.constraints.NotNull;

public record PostRequest(@NotNull(message = "Title can't be empty")
                          String title,
                          @NotNull(message = "Content can't be empty")
                          String content) {
}