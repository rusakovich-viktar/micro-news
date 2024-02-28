package by.clevertec.newsproject.client;

import by.clevertec.newsproject.dto.response.CommentResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "micro-comments", url = "http://localhost:8082")
public interface CommentClient {

    @DeleteMapping("/comments/news/{newsId}")
    ResponseEntity<Void> deleteCommentsByNewsId(@PathVariable Long newsId);

    @GetMapping("/comments/news/{newsId}")
    ResponseEntity<Page<CommentResponseDto>> getCommentsByNewsId(@PathVariable Long newsId, Pageable pageable);

}
