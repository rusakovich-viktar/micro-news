package by.clevertec.newsproject.cache.proxy;

import by.clevertec.newsproject.cache.Cache;
import by.clevertec.newsproject.cache.impl.LfuCache;
import by.clevertec.newsproject.cache.impl.LruCache;
import by.clevertec.newsproject.dto.response.NewsResponseDto;
import by.clevertec.newsproject.util.Constant.Messages;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.StampedLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Прокси-класс для управления кэшем новостей.
 */
@Slf4j
@Aspect
@Component
@Profile("dev")
@RequiredArgsConstructor
public class NewsProxy {

    @Value("${cache.algorithm}")
    private String algorithm;

    @Value("${cache.capacity}")
    private Integer maxCapacity;

    private final AtomicReference<Cache<Long, Object>> userCache = new AtomicReference<>(createCache());
    private final StampedLock lock = new StampedLock();

    /**
     * Получает новости из кэша или из базы данных, если в кэше их нет.
     *
     * @param joinPoint точка присоединения
     * @return объект новостей
     * @throws Throwable в случае ошибки
     */
    @SuppressWarnings("checkstyle:IllegalCatch")
    @Around("@annotation(org.springframework.cache.annotation.Cacheable) "
            + "&& execution(* by.clevertec.newsproject.service.NewsService.getNewsById(..))")
    public Object getNews(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Long id = (Long) args[0];

        long stamp = lock.tryOptimisticRead();
        Object cachedObject = userCache.get().get(id);
        NewsResponseDto newsResponseDto = null;
        if (cachedObject instanceof NewsResponseDto) {
            newsResponseDto = (NewsResponseDto) cachedObject;
        }
        if (!lock.validate(stamp)) {
            stamp = lock.readLock();
            try {
                newsResponseDto = (NewsResponseDto) userCache.get().get(id);
            } finally {
                lock.unlockRead(stamp);
            }
        }

        if (newsResponseDto != null) {
            log.debug(Messages.NEWS_WITH_ID_WAS_RETRIEVED_FROM_CACHE, id);
            return newsResponseDto;
        }

        log.debug(Messages.NEWS_WITH_ID_WAS_NOT_FOUND_IN_CACHE_RETRIEVING_FROM_DATABASE, id);
        Object result = joinPoint.proceed();
        if (result instanceof NewsResponseDto newsDto) {
            userCache.get().put(id, result);
            return newsDto;
        }
        return result;
    }

    /**
     * Добавляет новые новости в кэш после их создания.
     *
     * @param response объект новостей
     */
    @AfterReturning(pointcut = "@annotation(org.springframework.cache.annotation.CachePut) && "
            + "execution(* by.clevertec.newsproject.service.NewsService.createNews(..))", returning = "response")
    public void createNews(NewsResponseDto response) {
        userCache.get().put(response.getId(), response);
        log.debug(Messages.NEWS_WITH_ID_WAS_ADDED_TO_CACHE, response.getId());

    }

    /**
     * Удаляет новости из кэша после их удаления.
     *
     * @param id идентификатор новостей
     */
    @AfterReturning(pointcut = "@annotation(org.springframework.cache.annotation.CacheEvict) "
            + "&& execution(* by.clevertec.newsproject.service.NewsService.deleteNews(Long)) && args(id)",
            argNames = "id")
    public void deleteNews(Long id) {
        userCache.get().remove(id);
        log.debug(Messages.NEWS_WITH_ID_WAS_REMOVED_FROM_CACHE, id);

    }

    /**
     * Обновляет новости в кэше после их обновления.
     *
     * @param id     идентификатор новостей
     * @param retVal объект новостей
     */
    @AfterReturning(pointcut = "@annotation(org.springframework.cache.annotation.CachePut) &&"
            + " execution(* by.clevertec.newsproject.service.NewsService.updateNews(Long, ..)) && args(id, ..)",
            argNames = "id,retVal", returning = "retVal")
    public void updateNews(Long id, NewsResponseDto retVal) {
        userCache.get().put(id, retVal);
        log.debug(Messages.NEWS_WITH_ID_WAS_UPDATED_IN_CACHE, id);

    }

    /**
     * Создает кэш.
     *
     * @return объект кэша
     */
    private Cache<Long, Object> createCache() {
        if (maxCapacity == null) {
            maxCapacity = 50;
        }
        if ("LFU".equals(algorithm)) {
            return new LfuCache<>(maxCapacity);
        } else {
            return new LruCache<>(maxCapacity);
        }
    }
}
