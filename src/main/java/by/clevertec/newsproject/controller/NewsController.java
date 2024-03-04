package by.clevertec.newsproject.controller;

import by.clevertec.newsproject.dto.request.NewsRequestDto;
import by.clevertec.newsproject.dto.response.CommentListResponseDto;
import by.clevertec.newsproject.dto.response.NewsResponseDto;
import by.clevertec.newsproject.service.NewsService;
import by.clevertec.newsproject.util.Constant.BaseApi;
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
@RequestMapping(BaseApi.NEWS)
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    @PostMapping
    public ResponseEntity<NewsResponseDto> createNews(@Valid @RequestBody NewsRequestDto newsRequestDto) {
        return new ResponseEntity<>(
                newsService.createNews(newsRequestDto),
                HttpStatus.CREATED);
    }

    @GetMapping(BaseApi.ID)
    public ResponseEntity<NewsResponseDto> getNewsById(@PathVariable Long id) {
        NewsResponseDto newsById = newsService.getNewsById(id);
        return ResponseEntity.ok(newsById);
    }

    @PutMapping(BaseApi.ID)
    public ResponseEntity<NewsResponseDto> updateNews(@PathVariable Long id,
                                                      @Valid @RequestBody NewsRequestDto newsRequestDto) {
        return ResponseEntity.ok(newsService.updateNews(id, newsRequestDto));
    }

    @DeleteMapping(BaseApi.ID)
    public ResponseEntity<Void> deleteNews(@PathVariable Long id) {
        newsService.deleteNews(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<NewsResponseDto>> getAllNews(Pageable pageable) {
        return ResponseEntity.ok(newsService.getAllNews(pageable));
    }

    @GetMapping(BaseApi.NEWS_ID_COMMENTS)
    public ResponseEntity<CommentListResponseDto> getCommentsByNewsId(@PathVariable Long newsId,
                                                                      Pageable pageable) {
        return ResponseEntity.ok(newsService.getCommentsByNewsId(newsId, pageable));

    }

}
