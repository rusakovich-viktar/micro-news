package by.clevertec.newsproject.service.impl;

import static by.clevertec.newsproject.util.Constant.Atrubutes.NEWS;

import by.clevertec.exception.EntityNotFoundExceptionCustom;
import by.clevertec.newsproject.client.CommentClient;
import by.clevertec.newsproject.dto.request.NewsRequestDto;
import by.clevertec.newsproject.dto.response.CommentListResponseDto;
import by.clevertec.newsproject.dto.response.CommentResponseDto;
import by.clevertec.newsproject.dto.response.NewsResponseDto;
import by.clevertec.newsproject.entity.News;
import by.clevertec.newsproject.mapper.NewsMapper;
import by.clevertec.newsproject.repository.NewsRepository;
import by.clevertec.newsproject.service.NewsService;
import by.clevertec.newsproject.util.Constant.Messages;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

/**
 * Реализация сервиса для управления новостями.
 */
@Slf4j
@Service
@Transactional
@EnableCaching
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;
    private final NewsMapper newsMapper;
    private final CommentClient commentClient;

    /**
     * Создает новость.
     *
     * @param newsRequestDto Запрос на создание новости.
     * @return Ответ с созданной новостью.
     */
    @Override
    @Transactional
    @CachePut(value = NEWS, key = "#result.id")
    public NewsResponseDto createNews(NewsRequestDto newsRequestDto) {
        News news = newsMapper.toEntity(newsRequestDto);
        News savedNews = newsRepository.save(news);
        return newsMapper.toDto(savedNews);
    }

    /**
     * Получает новость по идентификатору.
     *
     * @param id Идентификатор новости.
     * @return Ответ с найденной новостью.
     */
    @Override
    @Cacheable(value = NEWS)
    @Transactional(readOnly = true)
    public NewsResponseDto getNewsById(Long id) {
        News news = newsRepository
                .findById(id)
                .orElseThrow(() ->
                        EntityNotFoundExceptionCustom.of(News.class, id));

        return newsMapper.toDto(news);
    }

    /**
     * Обновляет новость по идентификатору.
     *
     * @param id             Идентификатор новости.
     * @param newsRequestDto Запрос на обновление новости.
     * @return Ответ с обновленной новостью.
     */
    @Override
    @Transactional
    @CachePut(value = NEWS, key = "#id")
    public NewsResponseDto updateNews(Long id, NewsRequestDto newsRequestDto) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> EntityNotFoundExceptionCustom.of(News.class, id));
        newsMapper.updateFromDtoToEntity(newsRequestDto, news);
        News updatedNews = newsRepository.save(news);
        return newsMapper.toDto(updatedNews);
    }

    /**
     * Удаляет новость по идентификатору.
     *
     * @param id Идентификатор новости.
     */
    @Override
    @Transactional
    @CacheEvict(value = NEWS, key = "#id")
    public void deleteNews(Long id) {

        News news = newsRepository.findById(id)
                .orElseThrow(() -> EntityNotFoundExceptionCustom.of(News.class, id));

        commentClient.deleteCommentsByNewsId(id);

        newsRepository.delete(news);
    }

    /**
     * Получает все новости с пагинацией.
     *
     * @param pageable Параметры пагинации.
     * @return Ответ со списком новостей.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<NewsResponseDto> getAllNews(Pageable pageable) {
        return newsRepository.findAll(pageable)
                .map(newsMapper::toDto);
    }

    /**
     * Получает комментарии к новости по идентификатору новости с пагинацией.
     *
     * @param newsId   Идентификатор новости.
     * @param pageable Параметры пагинации.
     * @return Ответ со списком комментариев.
     */
    @Override
    @Transactional(readOnly = true)
    public CommentListResponseDto getCommentsByNewsId(Long newsId, Pageable pageable) {

        newsRepository.findById(newsId).orElseThrow(() ->
                EntityNotFoundExceptionCustom.of(News.class, newsId));

        ResponseEntity<Page<CommentResponseDto>> response = commentClient.getCommentsByNewsId(newsId, pageable);

        if (!response.getStatusCode().equals(HttpStatus.OK) || response.getBody() == null) {
            throw new RuntimeException(Messages.ERROR_RETRIEVING_COMMENTS_FOR_NEWS_ID + newsId);
        }

        return new CommentListResponseDto(response.getBody().getContent());
    }

}
