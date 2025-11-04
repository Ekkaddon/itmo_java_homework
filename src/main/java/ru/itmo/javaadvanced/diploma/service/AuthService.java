package ru.itmo.javaadvanced.diploma.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.itmo.javaadvanced.diploma.dto.auth.AuthResponse;
import ru.itmo.javaadvanced.diploma.dto.auth.LoginRequest;
import ru.itmo.javaadvanced.diploma.repository.UserAccountRepository;
import ru.itmo.javaadvanced.diploma.security.JwtService;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserAccountRepository userAccountRepository;

    public AuthService(AuthenticationManager authenticationManager,
                       JwtService jwtService,
                       UserAccountRepository userAccountRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userAccountRepository = userAccountRepository;
    }

    public AuthResponse authenticate(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        String username = authentication.getName();
        return userAccountRepository.findByUsername(username)
                .map(userAccount -> new AuthResponse(jwtService.issueToken(userAccount)))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}