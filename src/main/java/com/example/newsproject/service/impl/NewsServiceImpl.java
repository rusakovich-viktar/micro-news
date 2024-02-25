package com.example.newsproject.service.impl;

import by.clevertec.exception.EntityNotFoundExceptionCustom;
import com.example.newsproject.client.CommentClient;
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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional
@EnableCaching
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;
    private final NewsMapper newsMapper;
    private final CommentClient commentClient;

    @Override
    @Transactional
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
    @Transactional
    @CachePut(value = "news", key = "#id")
    public NewsResponseDto updateNews(Long id, NewsRequestDto newsRequestDto) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> EntityNotFoundExceptionCustom.of(News.class, id));
        newsMapper.updateFromDtoToEntity(newsRequestDto, news);
        News updatedNews = newsRepository.save(news);
        return newsMapper.toDto(updatedNews);
    }

    @CacheEvict(value = "news", key = "#id")
    @Override
    @Transactional
    public void deleteNews(Long id) {

        News news = newsRepository.findById(id)
                .orElseThrow(() -> EntityNotFoundExceptionCustom.of(News.class, id));

        commentClient.deleteCommentsByNewsId(id);

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
        ResponseEntity<Page<CommentResponseDto>> response = commentClient.getCommentsByNewsId(newsId, pageable);

        if (!response.getStatusCode().equals(HttpStatus.OK) || response.getBody() == null) {
            throw new RuntimeException("Error retrieving comments for newsId: " + newsId);
        }

        CommentListResponseDto commentListResponseDto = new CommentListResponseDto(response.getBody().getContent());

        return ResponseEntity.ok(commentListResponseDto);
    }

}
