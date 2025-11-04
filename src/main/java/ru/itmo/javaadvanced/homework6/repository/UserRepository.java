package ru.itmo.javaadvanced.homework6.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.itmo.javaadvanced.homework6.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}