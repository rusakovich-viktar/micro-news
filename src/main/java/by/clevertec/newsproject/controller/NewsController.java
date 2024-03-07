package by.clevertec.newsproject.controller;

import static by.clevertec.newsproject.util.Constant.BaseApi.ID;
import static by.clevertec.newsproject.util.Constant.BaseApi.NEWS;
import static by.clevertec.newsproject.util.Constant.BaseApi.NEWS_ID_COMMENTS;

import by.clevertec.newsproject.dto.request.NewsRequestDto;
import by.clevertec.newsproject.dto.response.CommentListResponseDto;
import by.clevertec.newsproject.dto.response.NewsResponseDto;
import by.clevertec.newsproject.service.NewsService;
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

/**
 * Контроллер для управления новостями.
 */
@RestController
@RequestMapping(NEWS)
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    /**
     * Создает новость.
     *
     * @param newsRequestDto Запрос на создание новости.
     * @return Ответ с созданной новостью.
     */
    @PostMapping
    public ResponseEntity<NewsResponseDto> createNews(@Valid @RequestBody NewsRequestDto newsRequestDto) {
        return new ResponseEntity<>(
                newsService.createNews(newsRequestDto),
                HttpStatus.CREATED);
    }

    /**
     * Получает новость по идентификатору.
     *
     * @param id Идентификатор новости.
     * @return Ответ с найденной новостью.
     */
    @GetMapping(ID)
    public ResponseEntity<NewsResponseDto> getNewsById(@PathVariable Long id) {
        NewsResponseDto newsById = newsService.getNewsById(id);
        return ResponseEntity.ok(newsById);
    }

    /**
     * Обновляет новость по идентификатору.
     *
     * @param id             Идентификатор новости.
     * @param newsRequestDto Запрос на обновление новости.
     * @return Ответ с обновленной новостью.
     */
    @PutMapping(ID)
    public ResponseEntity<NewsResponseDto> updateNews(@PathVariable Long id,
                                                      @Valid @RequestBody NewsRequestDto newsRequestDto) {
        return ResponseEntity.ok(newsService.updateNews(id, newsRequestDto));
    }

    /**
     * Удаляет новость по идентификатору.
     *
     * @param id Идентификатор новости.
     * @return Ответ без содержимого.
     */
    @DeleteMapping(ID)
    public ResponseEntity<Void> deleteNews(@PathVariable Long id) {
        newsService.deleteNews(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Получает все новости с пагинацией.
     *
     * @param pageable Параметры пагинации.
     * @return Ответ со списком новостей.
     */
    @GetMapping
    public ResponseEntity<Page<NewsResponseDto>> getAllNews(Pageable pageable) {
        return ResponseEntity.ok(newsService.getAllNews(pageable));
    }

    /**
     * Получает комментарии к новости по идентификатору новости с пагинацией.
     *
     * @param newsId   Идентификатор новости.
     * @param pageable Параметры пагинации.
     * @return Ответ со списком комментариев.
     */
    @GetMapping(NEWS_ID_COMMENTS)
    public ResponseEntity<CommentListResponseDto> getCommentsByNewsId(@PathVariable Long newsId,
                                                                      Pageable pageable) {
        return ResponseEntity.ok(newsService.getCommentsByNewsId(newsId, pageable));

    }

}
