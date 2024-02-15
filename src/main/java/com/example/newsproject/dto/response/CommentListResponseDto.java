package com.example.newsproject.dto.response;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CommentListResponseDto {

    private List<CommentResponseDto> content;

}
