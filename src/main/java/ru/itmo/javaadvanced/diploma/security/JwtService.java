package ru.itmo.javaadvanced.diploma.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.itmo.javaadvanced.diploma.domain.entity.UserAccount;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class JwtService {

    private final JwtProperties jwtProperties;

    public JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    public String issueToken(UserAccount userAccount) {
        Instant now = Instant.now();
        Instant expiresAt = now.plus(jwtProperties.accessTokenTtlMinutes().longValue(), ChronoUnit.MINUTES);
        byte[] signingKey = Decoders.BASE64.decode(jwtProperties.secret());

        return Jwts.builder()
                .setSubject(userAccount.getUsername())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiresAt))
                .claim("role", userAccount.getRole().name())
                .signWith(Keys.hmacShaKeyFor(signingKey))
                .compact();
    }

    public Claims parseClaims(String token) {
        try {
            byte[] signingKey = Decoders.BASE64.decode(jwtProperties.secret());

            return Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(signingKey))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (RuntimeException exception) {
            throw new BadCredentialsException("Invalid JWT token", exception);
        }
    }

    public String extractUsername(String token) {
        return parseClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        Claims claims = parseClaims(token);
        String username = claims.getSubject();
        Date expiration = claims.getExpiration();
        return username.equals(userDetails.getUsername()) && expiration.toInstant().isAfter(Instant.now());
    }
}