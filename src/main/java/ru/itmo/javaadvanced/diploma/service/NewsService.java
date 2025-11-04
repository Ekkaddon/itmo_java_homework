package ru.itmo.javaadvanced.diploma.service;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.itmo.javaadvanced.diploma.domain.entity.CrewMember;
import ru.itmo.javaadvanced.diploma.domain.entity.News;
import ru.itmo.javaadvanced.diploma.dto.news.NewsRequest;
import ru.itmo.javaadvanced.diploma.dto.news.NewsResponse;
import ru.itmo.javaadvanced.diploma.exception.ResourceNotFoundException;
import ru.itmo.javaadvanced.diploma.repository.CrewMemberRepository;
import ru.itmo.javaadvanced.diploma.repository.NewsRepository;
import ru.itmo.javaadvanced.diploma.security.UserAccountDetails;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class NewsService {

    private final NewsRepository newsRepository;
    private final CrewMemberRepository crewMemberRepository;

    public NewsService(NewsRepository newsRepository, CrewMemberRepository crewMemberRepository) {
        this.newsRepository = newsRepository;
        this.crewMemberRepository = crewMemberRepository;
    }

    public List<NewsResponse> findAllNews() {
        return newsRepository.findAll(Sort.by(Sort.Direction.DESC, "publishedAt"))
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public NewsResponse publishNews(NewsRequest newsRequest, UserAccountDetails authorDetails) {
        Long crewMemberId = authorDetails.userAccount().getCrewMember().getId();
        CrewMember author = crewMemberRepository.findById(crewMemberId)
                .orElseThrow(() -> new ResourceNotFoundException("Crew member not found"));

        News news = News.builder()
                .title(newsRequest.title())
                .content(newsRequest.content())
                .author(author)
                .publishedAt(OffsetDateTime.now())
                .build();

        return toResponse(newsRepository.save(news));
    }

    @Transactional
    public NewsResponse updateNews(Long id, NewsRequest newsRequest) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("News not found"));
        news.setTitle(newsRequest.title());
        news.setContent(newsRequest.content());
        return toResponse(news);
    }

    @Transactional
    public void deleteNews(Long id) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Новость не найдена"));
        newsRepository.delete(news);
    }

    private NewsResponse toResponse(News news) {
        CrewMember author = news.getAuthor();
        String authorFullName = author.getLastName() + " " + author.getFirstName()
                + (author.getMiddleName() != null ? " " + author.getMiddleName() : "");

        return new NewsResponse(
                news.getId(),
                news.getTitle(),
                news.getContent(),
                news.getPublishedAt(),
                authorFullName.trim()
        );
    }
}