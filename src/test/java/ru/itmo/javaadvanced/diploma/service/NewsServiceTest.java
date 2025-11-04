package ru.itmo.javaadvanced.diploma.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;
import ru.itmo.javaadvanced.diploma.domain.entity.CrewMember;
import ru.itmo.javaadvanced.diploma.domain.entity.News;
import ru.itmo.javaadvanced.diploma.domain.entity.UserAccount;
import ru.itmo.javaadvanced.diploma.domain.enums.CrewMemberType;
import ru.itmo.javaadvanced.diploma.dto.news.NewsRequest;
import ru.itmo.javaadvanced.diploma.dto.news.NewsResponse;
import ru.itmo.javaadvanced.diploma.exception.ResourceNotFoundException;
import ru.itmo.javaadvanced.diploma.repository.CrewMemberRepository;
import ru.itmo.javaadvanced.diploma.repository.NewsRepository;
import ru.itmo.javaadvanced.diploma.security.UserAccountDetails;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class NewsServiceTest {

    private NewsService newsService;
    private NewsRepository newsRepository;
    private CrewMemberRepository crewMemberRepository;

    @BeforeEach
    void setUp() {
        newsRepository = mock(NewsRepository.class);
        crewMemberRepository = mock(CrewMemberRepository.class);
        newsService = new NewsService(newsRepository, crewMemberRepository);
    }

    @Test
    @DisplayName("Получение всех новостей")
    void shouldFindAllNews() {
        // Given
        CrewMember author = new CrewMember();
        author.setFirstName("Дмитрий");
        author.setLastName("Кузнецов");
        author.setMiddleName("Алексеевич");

        News news1 = new News();
        news1.setId(1L);
        news1.setTitle("Новость 1");
        news1.setContent("Содержание 1");
        news1.setPublishedAt(OffsetDateTime.now());
        news1.setAuthor(author);

        News news2 = new News();
        news2.setId(2L);
        news2.setTitle("Новость 2");
        news2.setContent("Содержание 2");
        news2.setPublishedAt(OffsetDateTime.now());
        news2.setAuthor(author);

        when(newsRepository.findAll(any(Sort.class))).thenReturn(List.of(news1, news2));

        // When
        List<NewsResponse> result = newsService.findAllNews();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Новость 1", result.get(0).title());
        assertEquals("Новость 2", result.get(1).title());
    }

    @Test
    @DisplayName("Публикация новости")
    void shouldPublishNews() {
        // Given
        NewsRequest request = new NewsRequest("Новая новость", "Содержание новости");

        UserAccount userAccount = new UserAccount();
        userAccount.setId(1L);

        CrewMember crewMember = new CrewMember();
        crewMember.setId(1L);
        crewMember.setFirstName("Дмитрий");
        crewMember.setLastName("Кузнецов");
        crewMember.setCrewMemberType(CrewMemberType.DISPATCHER);

        userAccount.setCrewMember(crewMember);

        UserAccountDetails userDetails = new UserAccountDetails(userAccount);

        News savedNews = News.builder()
                .id(1L)
                .title(request.title())
                .content(request.content())
                .publishedAt(OffsetDateTime.now())
                .author(crewMember)
                .build();

        when(crewMemberRepository.findById(1L)).thenReturn(Optional.of(crewMember));
        when(newsRepository.save(any(News.class))).thenReturn(savedNews);

        // When
        NewsResponse result = newsService.publishNews(request, userDetails);

        // Then
        assertNotNull(result);
        assertEquals("Новая новость", result.title());
        assertEquals("Содержание новости", result.content());
        verify(newsRepository).save(any(News.class));
    }

    @Test
    @DisplayName("Обновление новости")
    void shouldUpdateNews() {
        // Given
        Long newsId = 1L;
        NewsRequest request = new NewsRequest("Обновленная новость", "Новое содержание");

        CrewMember author = new CrewMember();
        author.setFirstName("Дмитрий");
        author.setLastName("Кузнецов");

        News existingNews = new News();
        existingNews.setId(newsId);
        existingNews.setTitle("Старая новость");
        existingNews.setContent("Старое содержание");
        existingNews.setAuthor(author);
        existingNews.setPublishedAt(OffsetDateTime.now());

        when(newsRepository.findById(newsId)).thenReturn(Optional.of(existingNews));

        // When
        NewsResponse result = newsService.updateNews(newsId, request);

        // Then
        assertNotNull(result);
        assertEquals("Обновленная новость", result.title());
        assertEquals("Новое содержание", result.content());
    }

    @Test
    @DisplayName("Обновление несуществующей новости выбрасывает исключение")
    void shouldThrowExceptionWhenNewsNotFound() {
        // Given
        Long newsId = 999L;
        NewsRequest request = new NewsRequest("Новость", "Содержание");

        when(newsRepository.findById(newsId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> newsService.updateNews(newsId, request));
    }

    @Test
    @DisplayName("Удаление новости")
    void shouldDeleteNews() {
        // Given
        Long newsId = 1L;
        News news = new News();
        news.setId(newsId);

        when(newsRepository.findById(newsId)).thenReturn(Optional.of(news));

        // When
        newsService.deleteNews(newsId);

        // Then
        verify(newsRepository).delete(news);
    }

    @Test
    @DisplayName("Удаление несуществующей новости выбрасывает исключение")
    void shouldThrowExceptionWhenDeletingNonExistentNews() {
        // Given
        Long newsId = 999L;

        when(newsRepository.findById(newsId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> newsService.deleteNews(newsId));
    }
}