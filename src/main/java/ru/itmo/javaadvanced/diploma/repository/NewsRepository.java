package ru.itmo.javaadvanced.diploma.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itmo.javaadvanced.diploma.domain.entity.News;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
}