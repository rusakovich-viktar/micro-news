package by.clevertec.newsproject.client;

import by.clevertec.newsproject.dto.response.CommentResponseDto;
import by.clevertec.newsproject.util.Constant.BaseApi;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "micro-comments", url = "${comments-service.url}")
public interface CommentClient {

    @DeleteMapping(BaseApi.COMMENTS_NEWS_NEWS_ID)
    ResponseEntity<Void> deleteCommentsByNewsId(@PathVariable Long newsId);

    @GetMapping(BaseApi.COMMENTS_NEWS_NEWS_ID)
    ResponseEntity<Page<CommentResponseDto>> getCommentsByNewsId(@PathVariable Long newsId, Pageable pageable);

}
