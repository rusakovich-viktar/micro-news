package by.clevertec.newsproject.entity;

import static org.junit.jupiter.api.Assertions.*;

import by.clevertec.newsproject.dto.response.CommentResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class NewsTest {

    private News news;

    @BeforeEach
    void setUp() {
        news = new News();
    }

    @Test
    void testComments() {
        CommentResponseDto comment = new CommentResponseDto();
        news.setComments(List.of(comment));

        List<CommentResponseDto> comments = news.getComments();

        assertNotNull(comments);
        assertEquals(1, comments.size());
        assertEquals(comment, comments.get(0));
    }

    @Test
    void testPrePersist() {
        news.prePersist();

        LocalDateTime time = news.getTime();
        LocalDateTime updateTime = news.getUpdateTime();

        assertNotNull(time);
        assertNotNull(updateTime);
        assertEquals(time, updateTime);
    }

    @Test
    void testPreUpdate() throws Exception {
        news.prePersist(); // Устанавливаем начальное время
        LocalDateTime initialTime = news.getTime();

        Thread.sleep(1000);

        news.preUpdate(); // Обновляем время

        LocalDateTime updateTime = news.getUpdateTime();

        assertNotNull(updateTime);
        assertNotEquals(initialTime, updateTime);
    }
}