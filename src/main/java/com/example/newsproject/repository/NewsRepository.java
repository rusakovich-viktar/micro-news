package com.example.newsproject.repository;

import com.example.newsproject.entity.News;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NewsRepository extends JpaRepository<News, Long> {

    @Query(value = "SELECT n FROM News n WHERE " +
            "(:queryString is null or lower(n.title) like lower(concat('%', :queryString, '%'))) or " +
            "(:queryString is null or lower(n.text) like lower(concat('%', :queryString, '%')))")
    Page<News> search(@Param("queryString") String queryString, Pageable pageable);

}
