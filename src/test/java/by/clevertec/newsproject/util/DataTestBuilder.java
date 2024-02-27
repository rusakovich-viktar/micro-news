package by.clevertec.newsproject.util;

import static by.clevertec.newsproject.util.TestConstant.LOCAL_DATE_TIME;
import static by.clevertec.newsproject.util.TestConstant.NEWS_TEXT;
import static by.clevertec.newsproject.util.TestConstant.NEWS_TITLE;
import static by.clevertec.newsproject.util.TestConstant.USERNAME;

import by.clevertec.newsproject.dto.request.NewsRequestDto;
import by.clevertec.newsproject.dto.response.CommentListResponseDto;
import by.clevertec.newsproject.dto.response.CommentResponseDto;
import by.clevertec.newsproject.dto.response.NewsResponseDto;
import by.clevertec.newsproject.entity.News;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(setterPrefix = "with")
public class DataTestBuilder {

    @Builder.Default
    private Long id = TestConstant.ID_ONE;

    @Builder.Default
    private LocalDateTime time = LOCAL_DATE_TIME;

    @Builder.Default
    private LocalDateTime updateTime = LOCAL_DATE_TIME;

    @Builder.Default
    private String title = NEWS_TITLE;

    @Builder.Default
    private String text = NEWS_TEXT;

    @Builder.Default
    private String username = USERNAME;

    @Builder.Default
    private List<CommentResponseDto> comments = new ArrayList<>();

    public News buildNews() {
        News news = new News();

        news.setId(id);
        news.setTime(time);
        news.setUpdateTime(time);
        news.setTitle(title);
        news.setText(text);
        news.setComments(comments);

        return news;
    }


    public CommentResponseDto buildCommentResponseDto() {
        CommentResponseDto buildCommentResponseDto = new CommentResponseDto();

        buildCommentResponseDto.setId(id);
        buildCommentResponseDto.setTime(time);
        buildCommentResponseDto.setUpdateTime(time);
        buildCommentResponseDto.setText(text);
        buildCommentResponseDto.setUsername(username);
        buildCommentResponseDto.setNewsId(id);

        return buildCommentResponseDto;
    }

    public NewsResponseDto buildNewsResponseDto() {
        NewsResponseDto buildNewsResponseDto = new NewsResponseDto();

        buildNewsResponseDto.setId(id);
        buildNewsResponseDto.setTime(time);
        buildNewsResponseDto.setUpdateTime(time);
        buildNewsResponseDto.setTitle(title);
        buildNewsResponseDto.setText(text);

        return buildNewsResponseDto;
    }


    public NewsRequestDto buildNewsRequestDto() {
        NewsRequestDto buildNewsRequestDto = new NewsRequestDto();

        buildNewsRequestDto.setTitle(title);
        buildNewsRequestDto.setText(text);

        return buildNewsRequestDto;
    }


}
