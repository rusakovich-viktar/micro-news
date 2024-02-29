package by.clevertec.newsproject.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

import by.clevertec.newsproject.dto.request.NewsRequestDto;
import by.clevertec.newsproject.dto.response.NewsResponseDto;
import by.clevertec.newsproject.entity.News;
import by.clevertec.newsproject.entity.News.Fields;
import by.clevertec.newsproject.util.DataTestBuilder;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class NewsMapperTest {

    private final NewsMapper newsMapper = Mappers.getMapper(NewsMapper.class);

    @Test
    void testToDtoShouldReturnNewsResponseDto() {

        News news = DataTestBuilder.builder()
                .build()
                .buildNews();
        NewsResponseDto expected = DataTestBuilder.builder()
                .build()
                .buildNewsResponseDto();

        // when
        NewsResponseDto actual = newsMapper.toDto(news);

        // then
        assertThat(actual)
                .hasFieldOrPropertyWithValue(Fields.id, expected.getId())
                .hasFieldOrPropertyWithValue(Fields.time, expected.getTime())
                .hasFieldOrPropertyWithValue(Fields.updateTime, expected.getUpdateTime())
                .hasFieldOrPropertyWithValue(Fields.title, expected.getTitle())
                .hasFieldOrPropertyWithValue(Fields.text, expected.getText());

    }

    @Test
    void testToDtoShouldReturnNull_whenEntityIsNull() {
        // when
        NewsResponseDto actual = newsMapper.toDto(null);

        // then
        assertNull(actual);
    }

    @Test
    void testToEntityShouldReturnNews() {
        // given
        NewsRequestDto dto = DataTestBuilder.builder()
                .build()
                .buildNewsRequestDto();
        News expected = DataTestBuilder.builder()
                .build()
                .buildNews();

        // when
        News actual = newsMapper.toEntity(dto);

        // then
        assertThat(actual)
                .hasFieldOrPropertyWithValue(NewsResponseDto.Fields.title, expected.getTitle())
                .hasFieldOrPropertyWithValue(NewsResponseDto.Fields.text, expected.getText());

    }

    @Test
    void testToEntityShouldReturnNullWhenDtoIsNull() {
        // when
        News actual = newsMapper.toEntity(null);

        // then
        assertNull(actual);
    }


    @Test
    void testUpdateFromDtoToEntity() {
        // given
        NewsRequestDto dto = DataTestBuilder.builder()
                .build()
                .buildNewsRequestDto();
        News entity = DataTestBuilder.builder()
                .build()
                .buildNews();

        // when
        newsMapper.updateFromDtoToEntity(dto, entity);

        // then
        assertThat(entity)
                .hasFieldOrPropertyWithValue(News.Fields.title, dto.getTitle())
                .hasFieldOrPropertyWithValue(News.Fields.text, dto.getText());
    }

}