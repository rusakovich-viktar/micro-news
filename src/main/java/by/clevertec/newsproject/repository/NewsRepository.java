package by.clevertec.newsproject.repository;

import by.clevertec.newsproject.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Репозиторий для управления новостями.
 */
public interface NewsRepository extends JpaRepository<News, Long> {

}
