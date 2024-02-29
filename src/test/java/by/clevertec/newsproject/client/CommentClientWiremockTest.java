package by.clevertec.newsproject.client;

import static by.clevertec.newsproject.util.TestConstant.ID_ONE;
import static by.clevertec.newsproject.util.TestConstant.Path.COMMENTS_NEWS;
import static by.clevertec.newsproject.util.TestConstant.Path.SIZE_5_PAGE_0;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.common.ContentTypes.APPLICATION_JSON;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static wiremock.com.google.common.net.HttpHeaders.CONTENT_TYPE;

import by.clevertec.newsproject.dto.response.CommentResponseDto;
import by.clevertec.newsproject.util.DataTestBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest
@RequiredArgsConstructor
@WireMockTest(httpPort = 8082)
class CommentClientWiremockTest {


    private final CommentClient commentsClient;

    private ObjectMapper objectMapper;
    private final Long newsId = ID_ONE;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void testDeleteCommentsByNewsId() {

        stubFor(delete(urlEqualTo(COMMENTS_NEWS + newsId))
                .willReturn(aResponse()
                        .withStatus(200)));

        ResponseEntity<Void> responseEntity = commentsClient.deleteCommentsByNewsId(newsId);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testGetCommentsByNewsId() throws JsonProcessingException {
        Pageable pageable = PageRequest.of(0, 5);

        List<CommentResponseDto> commentList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            CommentResponseDto commentResponseDto = DataTestBuilder.builder()
                    .build()
                    .buildCommentResponseDto();
            commentList.add(commentResponseDto);
        }

        Page<CommentResponseDto> commentPage = new PageImpl<>(commentList, pageable, commentList.size());

        stubFor(get(urlEqualTo(COMMENTS_NEWS + newsId + SIZE_5_PAGE_0))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON)
                        .withBody(objectMapper.writeValueAsString(commentPage))));

        ResponseEntity<Page<CommentResponseDto>> responseEntity = commentsClient.getCommentsByNewsId(newsId, pageable);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(commentList.size(), responseEntity.getBody().getContent().size());
    }
}
