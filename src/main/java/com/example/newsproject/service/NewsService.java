package com.example.newsproject.service;

import com.example.newsproject.dto.request.NewsRequestDto;
import com.example.newsproject.dto.response.CommentListResponseDto;
import com.example.newsproject.dto.response.CommentResponseDto;
import com.example.newsproject.dto.response.NewsResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface NewsService {

    /*    Create */
    NewsResponseDto createNews(NewsRequestDto newsRequestDto);

    /*    Read*/
    NewsResponseDto getNewsById(Long id);

    /*  Update*/
    NewsResponseDto updateNews(Long id, NewsRequestDto newsRequestDto);

    /*Delete*/
    void deleteNews(Long id);

    /*Pageable*/
    Page<NewsResponseDto> getAllNews(Pageable pageable);

    /* -	просмотр новости с комментариями относящимися к ней (с пагинацией)*/
    ResponseEntity<CommentListResponseDto> getCommentsByNewsId(Long newsId, Pageable pageable);

    CommentResponseDto getCommentByNewsIdAndCommentId(Long newsId, Long commentId);

}
