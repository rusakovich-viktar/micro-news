package com.example.newsproject.service.impl;

import com.example.newsproject.dto.request.NewsRequestDto;
import com.example.newsproject.dto.response.NewsResponseDto;
import com.example.newsproject.entity.News;
import com.example.newsproject.exception.EntityNotFoundException;
import com.example.newsproject.mapper.NewsMapper;
import com.example.newsproject.repository.NewsRepository;
import com.example.newsproject.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;
    private final NewsMapper newsMapper;

    @Transactional
    @Override
    public NewsResponseDto createNews(NewsRequestDto newsRequestDto) {
        News news = newsMapper.toEntity(newsRequestDto);
        News savedNews = newsRepository.save(news);
        return newsMapper.toDto(savedNews);
    }

    @Transactional(readOnly = true)
    @Override
    public NewsResponseDto getNewsById(Long id) {
        News news = newsRepository
                .findById(id)
                .orElseThrow(() ->
                        EntityNotFoundException.of(News.class, id));
        return newsMapper.toDto(news);
    }


    @Transactional
    @Override
    public NewsResponseDto updateNews(Long id, NewsRequestDto newsRequestDto) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> EntityNotFoundException.of(News.class, id));
        newsMapper.updateFromDto(newsRequestDto, news);
        News updatedNews = newsRepository.save(news);
        return newsMapper.toDto(updatedNews);
    }

    @Transactional
    @Override
    public void deleteNews(Long id) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> EntityNotFoundException.of(News.class, id));
        newsRepository.delete(news);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<NewsResponseDto> getAllNews(Pageable pageable) {
        return newsRepository.findAll(pageable)
                .map(newsMapper::toDto);
    }
}
