package by.clevertec.newsproject.dto.response;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class CommentListResponseDto implements Serializable {

    private List<CommentResponseDto> comments;

}
