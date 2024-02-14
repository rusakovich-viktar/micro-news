package com.example.newsproject.controller;

import com.example.newsproject.dto.request.NewsRequestDto;
import com.example.newsproject.dto.response.NewsResponseDto;
import com.example.newsproject.service.NewsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
public class NewsController extends BaseController{

    private final NewsService newsService;


    @PostMapping
    public ResponseEntity<NewsResponseDto> createNews(@Valid @RequestBody NewsRequestDto newsRequestDto) {
        return new ResponseEntity<>(
                newsService.createNews(newsRequestDto),
                HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NewsResponseDto> getNewsById(@PathVariable Long id) {
        return ResponseEntity.ok(newsService.getNewsById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NewsResponseDto> updateNews(@PathVariable Long id,@Valid @RequestBody NewsRequestDto newsRequestDto) {
        return ResponseEntity.ok(newsService.updateNews(id, newsRequestDto));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNews(@PathVariable Long id) {
        newsService.deleteNews(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<NewsResponseDto>> getAllNews(Pageable pageable) {
        return ResponseEntity.ok(newsService.getAllNews(pageable));
    }
}