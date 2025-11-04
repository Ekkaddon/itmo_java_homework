package ru.itmo.javaadvanced.diploma.dto.news;

import java.time.OffsetDateTime;

public record NewsResponse(
        Long id,
        String title,
        String content,
        OffsetDateTime publishedAt,
        String authorFullName
) {
}