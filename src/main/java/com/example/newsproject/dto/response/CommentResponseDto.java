package com.example.newsproject.dto.response;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentResponseDto {

    private Long id;
    private LocalDateTime time;
    private LocalDateTime updateTime;
    private String text;
    private String username;
    private Long newsId;
}
