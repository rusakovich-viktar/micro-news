package by.clevertec.newsproject.dto.request;

import by.clevertec.newsproject.util.Constant.Messages;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

/**
 * Data Transfer Object (DTO) для создания или обновления новостей.
 */
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@FieldNameConstants
public class NewsRequestDto implements Serializable {

    /**
     * Заголовок новости.
     */
    @NotBlank(message = Messages.TITLE_CANNOT_BE_BLANK)
    private String title;

    /**
     * Текст новости.
     */
    @NotBlank(message = Messages.TEXT_CANNOT_BE_BLANK)
    private String text;

}
