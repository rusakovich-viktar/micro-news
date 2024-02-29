package by.clevertec.newsproject.service.impl;

import static by.clevertec.newsproject.util.TestConstant.ExceptionMessages.ERROR_RETRIEVING_COMMENTS_FOR_NEWS_ID;
import static by.clevertec.newsproject.util.TestConstant.ExceptionMessages.POSTFIX_NOT_FOUND_CUSTOM_MESSAGE;
import static by.clevertec.newsproject.util.TestConstant.ExceptionMessages.PREFIX_NOT_FOUND_CUSTOM_MESSAGE;
import static by.clevertec.newsproject.util.TestConstant.ID_ONE;
import static by.clevertec.newsproject.util.TestConstant.NEW_TEXT;
import static by.clevertec.newsproject.util.TestConstant.NEW_TITLE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import by.clevertec.exception.EntityNotFoundExceptionCustom;
import by.clevertec.newsproject.client.CommentClient;
import by.clevertec.newsproject.dto.request.NewsRequestDto;
import by.clevertec.newsproject.dto.response.CommentListResponseDto;
import by.clevertec.newsproject.dto.response.CommentResponseDto;
import by.clevertec.newsproject.dto.response.NewsResponseDto;
import by.clevertec.newsproject.entity.News;
import by.clevertec.newsproject.mapper.NewsMapper;
import by.clevertec.newsproject.repository.NewsRepository;
import by.clevertec.newsproject.util.DataTestBuilder;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class NewsServiceImplTest {


    @Mock
    private NewsRepository newsRepository;

    @Mock
    private NewsMapper newsMapper;

    @Mock
    private CommentClient commentClient;

    @InjectMocks
    private NewsServiceImpl newsService;

    private News news;

    @BeforeEach
    void setUp() {
        news = DataTestBuilder.builder()
                .build()
                .buildNews();
    }

    @Nested
    class CreateNewsTests {

        @Test
        void testCreateNewsShouldReturnNewsResponseDto_whenCorrectNewsRequestDto() {
            // given

            NewsRequestDto newsRequestDto = DataTestBuilder.builder()
                    .build()
                    .buildNewsRequestDto();

            NewsResponseDto expected = DataTestBuilder.builder()
                    .build()
                    .buildNewsResponseDto();

            when(newsMapper.toEntity(newsRequestDto))
                    .thenReturn(news);
            when(newsRepository.save(news))
                    .thenReturn(news);
            when(newsMapper.toDto(news))
                    .thenReturn(expected);

            // when
            NewsResponseDto actual = newsService.createNews(newsRequestDto);

            // then
            assertEquals(expected, actual);
            verify(newsRepository)
                    .save(news);
            verify(newsMapper)
                    .toDto(news);
        }
    }

    @Nested
    class GetNewsTests {

        @Test
        void testGetNewsByIdShouldReturnNewsResponseDto_whenNewsIsExist() {
            // given

            NewsResponseDto expected = DataTestBuilder.builder()
                    .build()
                    .buildNewsResponseDto();

            when(newsRepository.findById(ID_ONE))
                    .thenReturn(Optional.of(news));
            when(newsMapper.toDto(news))
                    .thenReturn(expected);

            // when
            NewsResponseDto actual = newsService.getNewsById(ID_ONE);

            // then
            assertEquals(expected, actual);
            verify(newsRepository)
                    .findById(ID_ONE);
            verify(newsMapper)
                    .toDto(news);
        }

        @Test
        void testGetNewsByIdShouldThrowNotFound_whenNewsDoesNotExist() {
            // given
            when(newsRepository.findById(ID_ONE)).thenReturn(Optional.empty());

            // when
            Exception exception = assertThrows(EntityNotFoundExceptionCustom.class, () ->
                    newsService.getNewsById(ID_ONE));

            // then
            assertEquals(News.class.getSimpleName() + PREFIX_NOT_FOUND_CUSTOM_MESSAGE + ID_ONE + POSTFIX_NOT_FOUND_CUSTOM_MESSAGE,
                    exception.getMessage());
        }
    }

    @Nested
    class UpdateNewsTests {

        @Test
        void testUpdateNewsShouldReturnNewsResponseDto_whenCorrectNewsRequestDto() {
            // given
            NewsRequestDto newsRequestDto = DataTestBuilder.builder()
                    .withTitle(NEW_TITLE)
                    .withText(NEW_TEXT)
                    .build()
                    .buildNewsRequestDto();


            when(newsRepository.findById(ID_ONE)).thenReturn(Optional.of(news));
            doAnswer(invocation -> {
                News target = invocation.getArgument(1);
                target.setTitle(newsRequestDto.getTitle());
                target.setText(newsRequestDto.getText());
                return null;
            }).when(newsMapper).updateFromDtoToEntity(newsRequestDto, news);

            // when
            newsService.updateNews(ID_ONE, newsRequestDto);

            // then
            verify(newsRepository)
                    .save(news);
            verify(newsMapper)
                    .updateFromDtoToEntity(newsRequestDto, news);
            assertEquals(newsRequestDto.getTitle(), news.getTitle());
            assertEquals(newsRequestDto.getText(), news.getText());

        }

        @Test
        void testUpdateNewsShouldThrowNotFound_whenNewsDoesNotExist() {
            // given
            NewsRequestDto newsRequestDto = DataTestBuilder.builder()
                    .build()
                    .buildNewsRequestDto();

            when(newsRepository.findById(ID_ONE))
                    .thenReturn(Optional.empty());

            // when
            Exception exception = assertThrows(EntityNotFoundExceptionCustom.class, () ->
                    newsService.updateNews(ID_ONE, newsRequestDto));

            // then
            assertEquals(News.class.getSimpleName() + PREFIX_NOT_FOUND_CUSTOM_MESSAGE + ID_ONE + POSTFIX_NOT_FOUND_CUSTOM_MESSAGE,
                    exception.getMessage());
        }
    }

    @Nested
    class DeleteNewsTests {

        @Test
        void testDeleteNewsShouldDeleteNews_whenNewsExist() {
            // given

            when(newsRepository.findById(ID_ONE))
                    .thenReturn(Optional.of(news));

            // when
            newsService
                    .deleteNews(ID_ONE);

            // then
            verify(commentClient)
                    .deleteCommentsByNewsId(ID_ONE);
            verify(newsRepository)
                    .delete(news);
        }

        @Test
        void testDeleteNewsShouldThrowNotFound_whenNewsDoesNotExist() {
            // given
            when(newsRepository.findById(ID_ONE)).thenReturn(Optional.empty());

            // when
            Exception exception = assertThrows(EntityNotFoundExceptionCustom.class, () ->
                    newsService.deleteNews(ID_ONE));

            // then
            assertEquals(News.class.getSimpleName() + PREFIX_NOT_FOUND_CUSTOM_MESSAGE + ID_ONE + POSTFIX_NOT_FOUND_CUSTOM_MESSAGE,
                    exception.getMessage());
        }
    }

    @Nested
    class GetAllNewsTests {

        @Test
        void testGetAllNewsShouldReturnListOfNews() {
            // given
            Pageable pageable = PageRequest.of(0, 5);
            Page<News> page = new PageImpl<>(List.of(news));

            NewsResponseDto newsResponseDto = DataTestBuilder.builder()
                    .build()
                    .buildNewsResponseDto();

            when(newsRepository.findAll(pageable))
                    .thenReturn(page);
            when(newsMapper.toDto(news))
                    .thenReturn(newsResponseDto);

            // when
            Page<NewsResponseDto> actual = newsService.getAllNews(pageable);

            // then
            assertEquals(1, actual.getContent().size());
            assertEquals(news.getTitle(), actual.getContent().get(0).getTitle());
            assertEquals(news.getText(), actual.getContent().get(0).getText());
            verify(newsRepository).findAll(pageable);
            verify(newsMapper).toDto(news);
        }

        @Test
        void testGetAllNewsShouldEmptyList_whenNoNewsExist() {
            // given
            Pageable pageable = PageRequest.of(0, 5);
            Page<News> page = new PageImpl<>(List.of());

            when(newsRepository.findAll(pageable))
                    .thenReturn(page);

            // when
            Page<NewsResponseDto> actual = newsService.getAllNews(pageable);

            // then
            assertTrue(actual.getContent().isEmpty());
        }
    }

    @Nested
    class GetCommentsByNewsIdTests {

        @Test
        void testGetCommentsByNewsIdShouldReturnCommentListResponseDto() {
            // given
            Pageable pageable = PageRequest.of(0, 5);
            CommentResponseDto comment = DataTestBuilder.builder()
                    .build()
                    .buildCommentResponseDto();
            Page<CommentResponseDto> page = new PageImpl<>(List.of(comment));

            when(newsRepository.findById(ID_ONE))
                    .thenReturn(Optional.of(news));
            when(commentClient.getCommentsByNewsId(ID_ONE, pageable))
                    .thenReturn(ResponseEntity.ok(page));

            // when
            CommentListResponseDto actual =
                    newsService.getCommentsByNewsId(ID_ONE, pageable);

            // then
            assertEquals(1, actual.getComments().size());
            assertEquals(comment.getText(), actual.getComments().get(0).getText());
            assertEquals(comment.getNewsId(), actual.getComments().get(0).getNewsId());
            verify(newsRepository).findById(ID_ONE);
        }

        @Test
        void testGetCommentsByNewsIdShouldEmptyList_whenNoComments() {
            // given
            Pageable pageable = PageRequest.of(0, 5);
            Page<CommentResponseDto> page = new PageImpl<>(List.of());

            when(newsRepository.findById(ID_ONE))
                    .thenReturn(Optional.of(news));
            when(commentClient.getCommentsByNewsId(ID_ONE, pageable))
                    .thenReturn(ResponseEntity.ok(page));

            // when
            CommentListResponseDto actual =
                    newsService.getCommentsByNewsId(ID_ONE, pageable);

            // then
            assertTrue(actual.getComments().isEmpty());
            verify(newsRepository).findById(ID_ONE);
        }

        @Test
        void testGetCommentsByNewsIdThrowException_whenClientReturnsError() {
            // given
            Pageable pageable = PageRequest.of(0, 5);

            when(commentClient.getCommentsByNewsId(ID_ONE, pageable))
                    .thenReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null));
            when(newsRepository.findById(ID_ONE))
                    .thenReturn(Optional.of(news));

            // when
            Exception exception = assertThrows(RuntimeException.class, () ->
                    newsService.getCommentsByNewsId(ID_ONE, pageable));

            // then
            assertEquals(ERROR_RETRIEVING_COMMENTS_FOR_NEWS_ID + ID_ONE, exception.getMessage());
        }
    }
}
