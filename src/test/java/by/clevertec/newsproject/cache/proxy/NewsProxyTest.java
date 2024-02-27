package by.clevertec.newsproject.cache.proxy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import by.clevertec.newsproject.cache.Cache;
import by.clevertec.newsproject.dto.request.NewsRequestDto;
import by.clevertec.newsproject.dto.response.NewsResponseDto;
import by.clevertec.newsproject.entity.News;
import by.clevertec.newsproject.mapper.NewsMapper;
import by.clevertec.newsproject.repository.NewsRepository;
import by.clevertec.newsproject.service.NewsService;
import by.clevertec.newsproject.util.DataTestBuilder;
import java.lang.reflect.Field;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.StampedLock;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@RequiredArgsConstructor
class NewsProxyTest {

    private final NewsProxy newsProxy;

    @MockBean
    private final NewsMapper newsMapper;

    @MockBean
    private final NewsRepository newsRepository;

    @MockBean
    private final NewsService newsService;

    @MockBean
    private ProceedingJoinPoint proceedingJoinPoint;

    private final StampedLock lock = new StampedLock();



    @Nested
    class TestGetNews {

        @Test
        void testGetNewsReturnNewsFromCache_whenNewsInCache() throws Throwable {
            NewsResponseDto newsResponseDto = DataTestBuilder.builder()
                    .build()
                    .buildNewsResponseDto();

            when(proceedingJoinPoint.getArgs())
                    .thenReturn(new Object[]{newsResponseDto.getId()});
            when(proceedingJoinPoint.proceed())
                    .thenReturn(newsResponseDto);

            newsProxy.createNews(newsResponseDto);

            Object result = newsProxy.getNews(proceedingJoinPoint);

            assertTrue(result instanceof NewsResponseDto);
            NewsResponseDto resultDto = (NewsResponseDto) result;
            assertEquals(newsResponseDto.getId(), resultDto.getId());
            assertEquals(newsResponseDto.getTime(), resultDto.getTime());
            assertEquals(newsResponseDto.getUpdateTime(), resultDto.getUpdateTime());
            assertEquals(newsResponseDto.getTitle(), resultDto.getTitle());
            assertEquals(newsResponseDto.getText(), resultDto.getText());

            verify(proceedingJoinPoint, times(0)).proceed();
        }

        @Test
        void testCreateNews() throws Throwable {

            News news = DataTestBuilder.builder()
                    .build()
                    .buildNews();

            NewsResponseDto expected = DataTestBuilder.builder()
                    .build()
                    .buildNewsResponseDto();

            NewsRequestDto newsRequestDto = DataTestBuilder.builder()
                    .build()
                    .buildNewsRequestDto();

            when(newsMapper.toEntity(newsRequestDto)).thenReturn(news);
            when(newsService.getNewsById(news.getId())).thenReturn(expected);

            newsProxy.createNews(expected);

            when(proceedingJoinPoint.getArgs()).thenReturn(new Object[]{news.getId()});
            when(proceedingJoinPoint.proceed()).thenReturn(expected);

            NewsResponseDto result = (NewsResponseDto) newsProxy.getNews(proceedingJoinPoint);

            assertEquals(expected.getId(), result.getId());

        }

        @Test
        void testGetNewsReturnNewsFromCache_whenOptimisticLockInvalidated() throws Throwable {
            NewsResponseDto newsResponseDto = DataTestBuilder.builder()
                    .build()
                    .buildNewsResponseDto();

            when(proceedingJoinPoint.getArgs())
                    .thenReturn(new Object[]{newsResponseDto.getId()});
            when(proceedingJoinPoint.proceed())
                    .thenReturn(newsResponseDto);

            newsProxy.createNews(newsResponseDto);

            // Invalidate the optimistic lock
            lock.writeLock();

            Object result = newsProxy.getNews(proceedingJoinPoint);

            assertTrue(result instanceof NewsResponseDto);
            NewsResponseDto resultDto = (NewsResponseDto) result;
            assertEquals(newsResponseDto.getId(), resultDto.getId());
            assertEquals(newsResponseDto.getTime(), resultDto.getTime());
            assertEquals(newsResponseDto.getUpdateTime(), resultDto.getUpdateTime());
            assertEquals(newsResponseDto.getTitle(), resultDto.getTitle());
            assertEquals(newsResponseDto.getText(), resultDto.getText());

            verify(proceedingJoinPoint, times(0)).proceed();
        }
    }


    @Test
    void testDeleteNews() throws Exception {
        News news = DataTestBuilder.builder()
                .build()
                .buildNews();

        Field userCacheField = NewsProxy.class.getDeclaredField("userCache");
        userCacheField.setAccessible(true);
        AtomicReference<Cache<Long, Object>> userCacheRef =
                (AtomicReference<Cache<Long, Object>>) userCacheField.get(newsProxy);

        Cache<Long, Object> userCache = userCacheRef.get();
        userCache.put(news.getId(), news);

        newsProxy.deleteNews(news.getId());

        assertNull(userCache.get(news.getId()));
    }

    @Test
    void testUpdateNews() throws Exception {
        News news = DataTestBuilder.builder()
                .build()
                .buildNews();

        NewsResponseDto expected = DataTestBuilder.builder()
                .build()
                .buildNewsResponseDto();

        when(newsRepository.findById(news.getId())).thenReturn(Optional.of(news));

        Field userCacheField = NewsProxy.class.getDeclaredField("userCache");
        userCacheField.setAccessible(true);
        AtomicReference<Cache<Long, Object>> userCacheRef =
                (AtomicReference<Cache<Long, Object>>) userCacheField.get(newsProxy);

        Cache<Long, Object> userCache = userCacheRef.get();

        newsProxy.updateNews(news.getId(), expected);

        assertEquals(expected, userCache.get(news.getId()));
    }


}