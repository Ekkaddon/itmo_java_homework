package ru.itmo.javaadvanced.diploma.security;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "app.security.jwt")
@Validated
public record JwtProperties(
        @NotBlank String secret,
        @NotNull @Positive Integer accessTokenTtlMinutes
) {
}