package com.example.newsproject.service.impl;

import by.clevertec.exception.EntityNotFoundExceptionCustom;
import com.example.newsproject.dto.request.NewsRequestDto;
import com.example.newsproject.dto.response.CommentListResponseDto;
import com.example.newsproject.dto.response.CommentResponseDto;
import com.example.newsproject.dto.response.NewsResponseDto;
import com.example.newsproject.entity.News;
import com.example.newsproject.mapper.NewsMapper;
import com.example.newsproject.repository.NewsRepository;
import com.example.newsproject.service.NewsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional
@EnableCaching
public class NewsServiceImpl implements NewsService {


    public final String PORT_TARGET_SERVICE = "8082";
    public final String PREFIX_HTTP = "http://";
    @Value("${map.value}")
    public String IP_TARGET_SERVICE;
    private final NewsRepository newsRepository;
    private final NewsMapper newsMapper;
    private final WebClient webClient;

    @Override
    @CachePut(value = "news", key = "#result.id")
    public NewsResponseDto createNews(NewsRequestDto newsRequestDto) {
        News news = newsMapper.toEntity(newsRequestDto);
        News savedNews = newsRepository.save(news);
        return newsMapper.toDto(savedNews);
    }

    @Transactional(readOnly = true)
    @Override
    @Cacheable(value = "news")
    public NewsResponseDto getNewsById(Long id) {
        News news = newsRepository
                .findById(id)
                .orElseThrow(() ->
                        EntityNotFoundExceptionCustom.of(News.class, id));
        return newsMapper.toDto(news);
    }

    @Override
    @CachePut(value = "news", key = "#id")
    public NewsResponseDto updateNews(Long id, NewsRequestDto newsRequestDto) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> EntityNotFoundExceptionCustom.of(News.class, id));
        newsMapper.updateFromDto(newsRequestDto, news);
        News updatedNews = newsRepository.save(news);
        return newsMapper.toDto(updatedNews);
    }

    @CacheEvict(value = "news", key = "#id")
    @Override
    public void deleteNews(Long id) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> EntityNotFoundExceptionCustom.of(News.class, id));
        newsRepository.delete(news);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<NewsResponseDto> getAllNews(Pageable pageable) {
        return newsRepository.findAll(pageable)
                .map(newsMapper::toDto);
    }

    @Transactional(readOnly = true)
    @Override
    public ResponseEntity<CommentListResponseDto> getCommentsByNewsId(Long newsId, Pageable pageable) {
        String commentsServiceUrl = PREFIX_HTTP + IP_TARGET_SERVICE + ":" + PORT_TARGET_SERVICE + "/news/" + newsId + "/comments";
        String url = commentsServiceUrl + "?page=" + pageable.getPageNumber() + "&size=" + pageable.getPageSize();
        CommentListResponseDto comments = webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(CommentListResponseDto.class)
                .block();
        return ResponseEntity.ok(comments);
    }

    @Override
    public CommentResponseDto getCommentByNewsIdAndCommentId(Long newsId, Long commentId) {
        String commentsServiceUrl = PREFIX_HTTP + IP_TARGET_SERVICE + ":" + PORT_TARGET_SERVICE + "/news/" + newsId + "/comments/" + commentId;
        return webClient.get()
                .uri(commentsServiceUrl)
                .retrieve()
                .bodyToMono(CommentResponseDto.class)
                .block();
    }

    public Page<News> search(String queryString, Pageable pageable) {
        return newsRepository.search(queryString, pageable);
    }

}
