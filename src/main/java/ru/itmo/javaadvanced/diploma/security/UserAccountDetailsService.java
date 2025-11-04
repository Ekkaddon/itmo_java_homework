package ru.itmo.javaadvanced.diploma.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.itmo.javaadvanced.diploma.repository.UserAccountRepository;

@Service
public class UserAccountDetailsService implements UserDetailsService {

    private final UserAccountRepository userAccountRepository;

    public UserAccountDetailsService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return userAccountRepository.findByUsername(username)
                .map(UserAccountDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}