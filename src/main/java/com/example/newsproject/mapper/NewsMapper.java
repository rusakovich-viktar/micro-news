package com.example.newsproject.mapper;

import com.example.newsproject.dto.request.NewsRequestDto;
import com.example.newsproject.dto.response.NewsResponseDto;
import com.example.newsproject.entity.News;
import java.util.List;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface NewsMapper {

    NewsResponseDto toDto(News entity);

    News toEntity(NewsRequestDto dto);

    List<NewsResponseDto> toDtoList(List<News> news);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(NewsRequestDto dto, @MappingTarget News entity);

}
