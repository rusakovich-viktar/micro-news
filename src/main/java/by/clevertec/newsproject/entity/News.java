package by.clevertec.newsproject.entity;

import by.clevertec.newsproject.dto.response.CommentResponseDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

/**
 * Сущность новости.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@FieldNameConstants
@Table(name = "news")
public class News implements Serializable {

    /**
     * Идентификатор новости.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Время создания новости.
     */
    @Column(nullable = false)
    private LocalDateTime time;

    /**
     * Время последнего обновления новости.
     */
    @Column(nullable = false)
    private LocalDateTime updateTime;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String text;

    /**
     * Список комментариев к новости (временное поле, не сохраняется в базе данных).
     */
    @Transient
    private List<CommentResponseDto> comments;

    /**
     * Метод, вызываемый перед сохранением новости.
     * Устанавливает время создания и обновления новости, если они не заданы.
     */
    @PrePersist
    public void prePersist() {
        if (this.time == null) {
            LocalDateTime now = LocalDateTime.now();
            this.time = now;
            this.updateTime = now;
        }
    }

    /**
     * Метод, вызываемый перед обновлением новости.
     * Устанавливает время обновления новости.
     */
    @PreUpdate
    public void preUpdate() {
        this.updateTime = LocalDateTime.now();
    }
}
