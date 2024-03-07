package by.clevertec.newsproject.mapper;

import by.clevertec.newsproject.dto.request.NewsRequestDto;
import by.clevertec.newsproject.dto.response.NewsResponseDto;
import by.clevertec.newsproject.entity.News;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

/**
 * Маппер для преобразования между сущностями и DTO новостей.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface NewsMapper {

    /**
     * Преобразует сущность новости в DTO.
     *
     * @param entity Сущность новости.
     * @return DTO новости.
     */
    NewsResponseDto toDto(News entity);

    /**
     * Преобразует DTO в сущность новости.
     *
     * @param dto DTO новости.
     * @return Сущность новости.
     */
    News toEntity(NewsRequestDto dto);

    /**
     * Обновляет сущность новости из DTO.
     *
     * @param dto    DTO новости.
     * @param entity Сущность новости.
     */
    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDtoToEntity(NewsRequestDto dto, @MappingTarget News entity);

}
