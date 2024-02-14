package com.example.newsproject.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NewsRequestDto {

    private Long id;
    private LocalDateTime time;

    @NotBlank(message = "Title cannot be blank")
    private String title;

    @NotBlank(message = "Text cannot be blank")
    private String text;
    private List<Long> commentIds;
}
