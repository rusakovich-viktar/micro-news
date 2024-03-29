package by.clevertec.newsproject.controller;

import static by.clevertec.newsproject.util.TestConstant.ExceptionMessages.EXCEPTION_OCCURRED_DURING_TEST;
import static by.clevertec.newsproject.util.TestConstant.ID;
import static by.clevertec.newsproject.util.TestConstant.ID_ONE;
import static by.clevertec.newsproject.util.TestConstant.NEW_TITLE;
import static by.clevertec.newsproject.util.TestConstant.Path.HTTP_LOCALHOST;
import static by.clevertec.newsproject.util.TestConstant.Path.NEWS;
import static by.clevertec.newsproject.util.TestConstant.Path.NEWS_URL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import by.clevertec.newsproject.dto.request.NewsRequestDto;
import by.clevertec.newsproject.dto.response.NewsResponseDto;
import by.clevertec.newsproject.entity.News;
import by.clevertec.newsproject.util.DataTestBuilder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("dev")
@RequiredArgsConstructor
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NewsControllerIntegrationTest {

    private final TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Test
    public void testMultithreadedAccess() throws InterruptedException {
        int numThreads = 6;
        ExecutorService service = Executors.newFixedThreadPool(numThreads);

        try {
            for (int i = 0; i < numThreads; i++) {
                service.submit(() -> {
                    try {
                        News newNews = DataTestBuilder.builder()
                                .build()
                                .buildNews();

                        ResponseEntity<Void> postResponse = restTemplate
                                .postForEntity(HTTP_LOCALHOST + port + NEWS, newNews, Void.class);
                        assertEquals(HttpStatus.CREATED, postResponse.getStatusCode());

                        Long id = newNews.getId();

                        ResponseEntity<NewsResponseDto> getResponse = restTemplate
                                .getForEntity(HTTP_LOCALHOST + port + NEWS_URL + ID,
                                        NewsResponseDto.class, id);
                        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
                        assertEquals(newNews.getTitle(), getResponse.getBody().getTitle());

                        NewsRequestDto updatedNews = DataTestBuilder.builder()
                                .withTitle(NEW_TITLE)
                                .build()
                                .buildNewsRequestDto();

                        restTemplate.put(HTTP_LOCALHOST + port + NEWS_URL + ID, updatedNews, ID_ONE);

                        ResponseEntity<NewsResponseDto> updatedGetResponse = restTemplate
                                .getForEntity(HTTP_LOCALHOST + port + NEWS_URL + ID,
                                        NewsResponseDto.class, id);
                        assertEquals(HttpStatus.OK, updatedGetResponse.getStatusCode());
                        assertEquals(updatedNews.getTitle(), updatedGetResponse.getBody().getTitle());

                        restTemplate.delete(HTTP_LOCALHOST + port + NEWS_URL + ID, ID_ONE);

                        ResponseEntity<NewsResponseDto> deletedGetResponse = restTemplate
                                .getForEntity(HTTP_LOCALHOST + port + NEWS_URL + ID,
                                        NewsResponseDto.class, id);
                        assertEquals(HttpStatus.NOT_FOUND, deletedGetResponse.getStatusCode());
                    } catch (Exception e) {
                        fail(EXCEPTION_OCCURRED_DURING_TEST + e.getMessage());
                    }
                });
            }
        } finally {
            service.shutdown();
            service.awaitTermination(1, TimeUnit.MINUTES);
        }
    }
}
