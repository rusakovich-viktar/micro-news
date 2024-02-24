package com.example.newsproject.controller;

import com.example.newsproject.entity.News;
import com.example.newsproject.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final NewsService newsService;

    @GetMapping()
    public ResponseEntity<Page<News>> search(@RequestParam String query, Pageable pageable) {
        Page<News> results = newsService.search(query, pageable);
        return ResponseEntity.ok(results);
    }
}
