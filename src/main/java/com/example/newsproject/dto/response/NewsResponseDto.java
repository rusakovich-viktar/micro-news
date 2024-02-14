package com.example.newsproject.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NewsResponseDto {

    private Long id;
    private LocalDateTime time;
    private LocalDateTime updateTime;
    private String title;
    private String text;
    private List<Long> commentIds;
}
