package by.clevertec.newsproject.controller;

import static by.clevertec.newsproject.util.TestConstant.ID_ONE;
import static by.clevertec.newsproject.util.TestConstant.Path.API_NEWS;
import static by.clevertec.newsproject.util.TestConstant.Path.API_NEWS_URL;
import static by.clevertec.newsproject.util.TestConstant.Path.HTTP_LOCALHOST;
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
                                .postForEntity(HTTP_LOCALHOST + port + API_NEWS, newNews, Void.class);
                        assertEquals(HttpStatus.CREATED, postResponse.getStatusCode());

                        Long id = newNews.getId();

                        ResponseEntity<NewsResponseDto> getResponse = restTemplate
                                .getForEntity(HTTP_LOCALHOST + port + API_NEWS_URL + "{id}",
                                        NewsResponseDto.class, id);
                        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
                        assertEquals(newNews.getTitle(), getResponse.getBody().getTitle());

                        NewsRequestDto updatedNews = DataTestBuilder.builder()
                                .withTitle("newTitle")
                                .build()
                                .buildNewsRequestDto();

                        restTemplate.put(HTTP_LOCALHOST + port + API_NEWS_URL + "{id}", updatedNews, ID_ONE);

                        ResponseEntity<NewsResponseDto> updatedGetResponse = restTemplate
                                .getForEntity(HTTP_LOCALHOST + port + API_NEWS_URL + "{id}",
                                        NewsResponseDto.class, id);
                        assertEquals(HttpStatus.OK, updatedGetResponse.getStatusCode());
                        assertEquals(updatedNews.getTitle(), updatedGetResponse.getBody().getTitle());

                        restTemplate.delete(HTTP_LOCALHOST + port + API_NEWS_URL + "{id}", ID_ONE);

                        ResponseEntity<NewsResponseDto> deletedGetResponse = restTemplate
                                .getForEntity(HTTP_LOCALHOST + port + API_NEWS_URL + "{id}",
                                        NewsResponseDto.class, id);
                        assertEquals(HttpStatus.NOT_FOUND, deletedGetResponse.getStatusCode());
                    } catch (Exception e) {
                        fail("Exception occurred during test: " + e.getMessage());
                    }
                });
            }
        } finally {
            service.shutdown();
            service.awaitTermination(1, TimeUnit.MINUTES);
        }
    }
}
