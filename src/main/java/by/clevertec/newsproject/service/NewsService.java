package by.clevertec.newsproject.service;

import by.clevertec.newsproject.dto.request.NewsRequestDto;
import by.clevertec.newsproject.dto.response.CommentListResponseDto;
import by.clevertec.newsproject.dto.response.NewsResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NewsService {

    NewsResponseDto createNews(NewsRequestDto newsRequestDto);

    NewsResponseDto getNewsById(Long id);

    NewsResponseDto updateNews(Long id, NewsRequestDto newsRequestDto);

    void deleteNews(Long id);

    Page<NewsResponseDto> getAllNews(Pageable pageable);

    CommentListResponseDto getCommentsByNewsId(Long newsId, Pageable pageable);

}
