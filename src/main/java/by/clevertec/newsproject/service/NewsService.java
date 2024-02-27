package by.clevertec.newsproject.service;

import by.clevertec.newsproject.dto.response.NewsResponseDto;
import by.clevertec.newsproject.dto.request.NewsRequestDto;
import by.clevertec.newsproject.dto.response.CommentListResponseDto;
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

    //    /* -	просмотр новости с комментариями относящимися к ней (с пагинацией)*/
    ResponseEntity<CommentListResponseDto> getCommentsByNewsId(Long newsId, Pageable pageable);

}
