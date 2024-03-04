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

@Entity
@Getter
@Setter
@Table(name = "news")
@NoArgsConstructor
@FieldNameConstants
public class News implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private LocalDateTime time;
    @Column(nullable = false)
    private LocalDateTime updateTime;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String text;

    @Transient
    private List<CommentResponseDto> comments;

    @PrePersist
    public void prePersist() {
        if (this.time == null) {
            LocalDateTime now = LocalDateTime.now();
            this.time = now;
            this.updateTime = now;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updateTime = LocalDateTime.now();
    }
}

