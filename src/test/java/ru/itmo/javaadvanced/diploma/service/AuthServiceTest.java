package ru.itmo.javaadvanced.diploma.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.itmo.javaadvanced.diploma.domain.entity.UserAccount;
import ru.itmo.javaadvanced.diploma.dto.auth.AuthResponse;
import ru.itmo.javaadvanced.diploma.dto.auth.LoginRequest;
import ru.itmo.javaadvanced.diploma.repository.UserAccountRepository;
import ru.itmo.javaadvanced.diploma.security.JwtService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    private AuthService authService;
    private AuthenticationManager authenticationManager;
    private UserAccountRepository userAccountRepository;
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        authenticationManager = mock(AuthenticationManager.class);
        userAccountRepository = mock(UserAccountRepository.class);
        jwtService = mock(JwtService.class);
        authService = new AuthService(authenticationManager, jwtService, userAccountRepository);
    }

    @Test
    @DisplayName("Успешная аутентификация возвращает JWT токен")
    void shouldAuthenticateSuccessfully() {
        LoginRequest loginRequest = new LoginRequest("testuser", "password");
        UserAccount userAccount = new UserAccount();
        userAccount.setUsername("testuser");

        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getName()).thenReturn("testuser");
        when(userAccountRepository.findByUsername("testuser")).thenReturn(java.util.Optional.of(userAccount));
        when(jwtService.issueToken(userAccount)).thenReturn("jwt-token-123");

        AuthResponse response = authService.authenticate(loginRequest);

        assertNotNull(response);
        assertEquals("jwt-token-123", response.accessToken());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService).issueToken(userAccount);
    }

    @Test
    @DisplayName("Неверные учетные данные выбрасывают исключение")
    void shouldThrowExceptionOnInvalidCredentials() {
        LoginRequest loginRequest = new LoginRequest("testuser", "wrongpassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        assertThrows(BadCredentialsException.class, () -> authService.authenticate(loginRequest));
    }

    @Test
    @DisplayName("Пользователь не найден выбрасывает исключение")
    void shouldThrowExceptionWhenUserNotFound() {
        LoginRequest loginRequest = new LoginRequest("testuser", "password");
        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getName()).thenReturn("testuser");
        when(userAccountRepository.findByUsername("testuser")).thenReturn(java.util.Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> authService.authenticate(loginRequest));
    }
}