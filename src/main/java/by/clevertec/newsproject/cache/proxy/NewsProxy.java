package by.clevertec.newsproject.cache.proxy;


import by.clevertec.newsproject.cache.Cache;
import by.clevertec.newsproject.cache.impl.LfuCache;
import by.clevertec.newsproject.cache.impl.LruCache;
import by.clevertec.newsproject.dto.response.NewsResponseDto;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.StampedLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * Класс {@code UserProxy} представляет собой прокси для работы с пользователями.
 * Он использует аспектно-ориентированное программирование (AOP) для кэширования пользователей.
 * <p>
 * Этот класс использует {@code Cache<Integer, User>} для хранения пользователей, где ключом является идентификатор пользователя.
 * Кэш настраивается с помощью метода {@code configureCache()}, который читает конфигурацию из файла YAML.
 * <p>
 * Методы {@code getUser()}, {@code createUser()}, {@code deleteUser()} и {@code updateUser()} объявлены как точки среза (pointcuts) для AOP.
 * Они используются в аннотациях {@code Around} и {@code AfterReturning} для выполнения действий перед, после или вместо вызова соответствующих методов сервиса.
 * <p>
 * Каждый из этих методов записывает информацию в журнал и обновляет кэш соответствующим образом.
 */
@Log4j2
@Aspect
@RequiredArgsConstructor
@Component
@Profile("dev")
public class NewsProxy {

    @Value("${cache.algorithm}")
    private String algorithm;

    @Value("${cache.capacity}")
    private Integer maxCapacity;

    private final AtomicReference<Cache<Long, Object>> userCache = new AtomicReference<>(createCache());
    private final StampedLock lock = new StampedLock();


    @SuppressWarnings("checkstyle:IllegalCatch")
    @Around("@annotation(org.springframework.cache.annotation.Cacheable) " +
            "&& execution(* by.clevertec.newsproject.service.NewsService.getNewsById(..))")
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
            log.info("News with id {} was retrieved from cache", id);
            return newsResponseDto;
        }

        log.info("News with id {} was not found in cache, retrieving from database", id);
        Object result = joinPoint.proceed();
        if (result instanceof NewsResponseDto newsDto) {
            userCache.get().put(id, result);
            return newsDto;
        }
        return result;
    }


    @AfterReturning(pointcut = "@annotation(org.springframework.cache.annotation.CachePut) && " +
            "execution(* by.clevertec.newsproject.service.NewsService.createNews(..))", returning = "response")
    public void createNews(NewsResponseDto response) {
        userCache.get().put(response.getId(), response);
        log.info("News with id {} was added to cache", response.getId());

    }


    @AfterReturning(pointcut = "@annotation(org.springframework.cache.annotation.CacheEvict) " +
            "&& execution(* by.clevertec.newsproject.service.NewsService.deleteNews(Long)) && args(id)",
            argNames = "id")
    public void deleteNews(Long id) {
        userCache.get().remove(id);
        log.info("News with id {} was removed from cache", id);

    }

    @AfterReturning(pointcut = "@annotation(org.springframework.cache.annotation.CachePut) &&" +
            " execution(* by.clevertec.newsproject.service.NewsService.updateNews(Long, ..)) && args(id, ..)",
            argNames = "id,retVal", returning = "retVal")
    public void updateNews(Long id, NewsResponseDto retVal) {
        userCache.get().put(id, retVal);
        log.info("News with id {} was updated in cache", id);


    }

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
