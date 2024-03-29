package by.clevertec.newsproject.dto.response;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;

/**
 * Класс для передачи данных (ответа) о новости.
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@FieldNameConstants
public class NewsResponseDto implements Serializable {

    private Long id;
    private LocalDateTime time;
    private LocalDateTime updateTime;
    private String title;
    private String text;
}
