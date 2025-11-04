package ru.itmo.javaadvanced.diploma.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.itmo.javaadvanced.diploma.dto.news.NewsRequest;
import ru.itmo.javaadvanced.diploma.dto.news.NewsResponse;
import ru.itmo.javaadvanced.diploma.security.UserAccountDetails;
import ru.itmo.javaadvanced.diploma.service.NewsService;

import java.util.List;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @Operation(summary = "Получение списка новостей")
    @GetMapping
    public ResponseEntity<List<NewsResponse>> findAllNews() {
        return ResponseEntity.ok(newsService.findAllNews());
    }

    @Operation(summary = "Публикация новости")
    @PostMapping
    @PreAuthorize("hasRole('DISPATCHER')")
    public ResponseEntity<NewsResponse> publishNews(@Validated @RequestBody NewsRequest newsRequest,
                                                    Authentication authentication) {
        UserAccountDetails authorDetails = extractUserAccountDetails(authentication);
        NewsResponse createdNews = newsService.publishNews(newsRequest, authorDetails);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdNews);
    }

    @Operation(summary = "Обновление новости")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('DISPATCHER')")
    public ResponseEntity<NewsResponse> updateNews(@PathVariable Long id,
                                                   @Validated @RequestBody NewsRequest newsRequest) {
        return ResponseEntity.ok(newsService.updateNews(id, newsRequest));
    }

    @Operation(summary = "Удаление новости")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('DISPATCHER')")
    public ResponseEntity<Void> deleteNews(@PathVariable Long id) {
        newsService.deleteNews(id);
        return ResponseEntity.noContent().build();
    }

    private UserAccountDetails extractUserAccountDetails(Authentication authentication) {
        if (authentication == null) {
            throw new IllegalStateException("Требуется аутентифицированный пользователь");
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserAccountDetails userAccountDetails) {
            return userAccountDetails;
        }
        throw new IllegalStateException("Требуется аутентифицированный пользователь");
    }
}