package by.clevertec.newsproject.controller;

import static by.clevertec.newsproject.util.TestConstant.ExceptionMessages.POSTFIX_NOT_FOUND_CUSTOM_MESSAGE;
import static by.clevertec.newsproject.util.TestConstant.ExceptionMessages.PREFIX_NOT_FOUND_CUSTOM_MESSAGE;
import static by.clevertec.newsproject.util.TestConstant.ID_ONE;
import static by.clevertec.newsproject.util.TestConstant.INVALID_ID;
import static by.clevertec.newsproject.util.TestConstant.NEWS1;
import static by.clevertec.newsproject.util.TestConstant.PAGE;
import static by.clevertec.newsproject.util.TestConstant.PAGE_NUMBER;
import static by.clevertec.newsproject.util.TestConstant.PAGE_SIZE;
import static by.clevertec.newsproject.util.TestConstant.Path.COMMENTS;
import static by.clevertec.newsproject.util.TestConstant.Path.NEWS;
import static by.clevertec.newsproject.util.TestConstant.Path.NEWS_URL;
import static by.clevertec.newsproject.util.TestConstant.SIZE;
import static by.clevertec.newsproject.util.TestConstant.STRING_ONE;
import static by.clevertec.newsproject.util.TestConstant.STRING_ONE_FIVE;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import by.clevertec.exception.EntityNotFoundExceptionCustom;
import by.clevertec.newsproject.dto.request.NewsRequestDto;
import by.clevertec.newsproject.dto.response.CommentListResponseDto;
import by.clevertec.newsproject.dto.response.CommentResponseDto;
import by.clevertec.newsproject.dto.response.NewsResponseDto;
import by.clevertec.newsproject.service.NewsService;
import by.clevertec.newsproject.util.DataTestBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@RequiredArgsConstructor
@WebMvcTest(NewsController.class)
class NewsControllerTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @MockBean
    private final NewsService newsService;

    @Nested
    class TestGetById {

        @Test
        void getByIdShouldReturnNewsResponseDto() throws Exception {

            NewsResponseDto expected = DataTestBuilder.builder()
                    .build()
                    .buildNewsResponseDto();

            when(newsService.getNewsById(expected.getId()))
                    .thenReturn(expected);

            mockMvc.perform(get(NEWS_URL + expected.getId()))
                    .andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(expected)));

        }

        @Test
        void getByIdShouldThrowNotFound_whenInvalidId() throws Exception {

            Long invalidId = INVALID_ID;
            String url = NEWS_URL + invalidId;
            EntityNotFoundExceptionCustom exception = new EntityNotFoundExceptionCustom
                    (NEWS1 + PREFIX_NOT_FOUND_CUSTOM_MESSAGE + invalidId
                            + POSTFIX_NOT_FOUND_CUSTOM_MESSAGE);

            when(newsService.getNewsById(invalidId))
                    .thenThrow(exception);

            mockMvc.perform(get(url))
                    .andExpect(status().isNotFound())
                    .andExpect(result -> assertEquals(exception.getMessage(),
                            result.getResolvedException().getMessage()));
        }
    }

    @Nested
    class TestCreateNews {

        @Test
        void createNewsShouldReturnNewsResponseDto() throws Exception {

            NewsRequestDto newsRequestDto = DataTestBuilder.builder()
                    .build()
                    .buildNewsRequestDto();

            NewsResponseDto expected = DataTestBuilder.builder()
                    .build()
                    .buildNewsResponseDto();

            when(newsService.createNews(newsRequestDto))
                    .thenReturn(expected);

            mockMvc.perform(post(NEWS)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(newsRequestDto)))
                    .andExpect(status().isCreated())
                    .andExpect(content().json(objectMapper.writeValueAsString(expected)));

            ArgumentCaptor<NewsRequestDto> captor = ArgumentCaptor.forClass(NewsRequestDto.class);
            verify(newsService, times(PAGE_NUMBER))
                    .createNews(captor.capture());

            NewsRequestDto capturedDto = captor.getValue();

            assertEquals(newsRequestDto, capturedDto);

        }

        @Test
        void createNewsShouldReturnBadRequest_whenInvalidRequest() throws Exception {

            NewsRequestDto invalidNewsRequestDto = new NewsRequestDto();

            mockMvc.perform(post(NEWS_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidNewsRequestDto)))
                    .andExpect(status().isNotFound());

            verify(newsService, times(0))
                    .createNews(any());
        }
    }

    @Nested
    class TestUpdateNews {

        @Test
        void updateNewsShouldReturnNewsResponseDto() throws Exception {

            NewsRequestDto newsRequestDto = DataTestBuilder.builder()
                    .build()
                    .buildNewsRequestDto();

            NewsResponseDto expected = DataTestBuilder.builder()
                    .build()
                    .buildNewsResponseDto();

            when(newsService.updateNews(ID_ONE, newsRequestDto))
                    .thenReturn(expected);

            mockMvc.perform(put(NEWS_URL + ID_ONE)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(newsRequestDto)))
                    .andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(expected)));

            ArgumentCaptor<NewsRequestDto> captor = ArgumentCaptor.forClass(NewsRequestDto.class);
            verify(newsService, times(PAGE_NUMBER))
                    .updateNews(eq(ID_ONE), captor.capture());

            NewsRequestDto capturedDto = captor.getValue();

            assertEquals(newsRequestDto, capturedDto);
        }

        @Test
        void updateNewsShouldReturnBadRequest_whenInvalidRequest() throws Exception {

            NewsRequestDto invalidNewsRequestDto = new NewsRequestDto();

            mockMvc.perform(put(NEWS_URL + ID_ONE)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidNewsRequestDto)))
                    .andExpect(status().isBadRequest());

            verify(newsService, times(0))
                    .updateNews(anyLong(), any());
        }

    }

    @Nested
    class TestDeleteNews {

        @Test
        void deleteNewsShouldReturnNoContent() throws Exception {

            mockMvc.perform(delete(NEWS_URL + ID_ONE))
                    .andExpect(status().isNoContent());

            verify(newsService, times(PAGE_NUMBER))
                    .deleteNews(ID_ONE);
        }
    }

    @Nested
    class TestGetAllNews {

        @Test
        void getAllNewsShouldReturnListOfNews() throws Exception {
            int pageNumber = PAGE_NUMBER;
            int pageSize = PAGE_SIZE;
            NewsResponseDto newsOne = DataTestBuilder.builder()
                    .build()
                    .buildNewsResponseDto();

            NewsResponseDto newsTwo = DataTestBuilder.builder()
                    .build()
                    .buildNewsResponseDto();

            List<NewsResponseDto> expectedNews = List.of(newsOne, newsTwo);
            when(newsService.getAllNews(PageRequest.of(pageNumber, pageSize)))
                    .thenReturn(new PageImpl<>(expectedNews));

            mockMvc.perform(get(NEWS)
                            .param(PAGE, String.valueOf(pageNumber))
                            .param(SIZE, String.valueOf(pageSize)))
                    .andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(new PageImpl<>(expectedNews))))
                    .andExpect(jsonPath("$.content", hasSize(expectedNews.size())))
                    .andExpect(jsonPath("$.content[0].id", is(newsOne.getId().intValue())))
                    .andExpect(jsonPath("$.content[0].title", is(newsOne.getTitle())))
                    .andExpect(jsonPath("$.content[0].text", is(newsOne.getText())))
                    .andExpect(jsonPath("$.content[1].id", is(newsTwo.getId().intValue())))
                    .andExpect(jsonPath("$.content[1].title", is(newsTwo.getTitle())))
                    .andExpect(jsonPath("$.content[1].text", is(newsTwo.getText())));

            verify(newsService, times(PAGE_NUMBER))
                    .getAllNews(PageRequest.of(pageNumber, pageSize));
        }
    }

    @Nested
    class TestGetCommentsByNewsId {

        @Test
        void getCommentsByNewsIdShouldReturnCommentListResponseDto() throws Exception {

            Long newsId = 1L;
            int pageNumber = PAGE_NUMBER;
            int pageSize = PAGE_SIZE;

            CommentResponseDto commentOne = DataTestBuilder.builder()
                    .build()
                    .buildCommentResponseDto();
            CommentResponseDto commentTwo = DataTestBuilder.builder()
                    .build()
                    .buildCommentResponseDto();

            List<CommentResponseDto> expectedComments = List.of(commentOne, commentTwo);

            CommentListResponseDto expected = new CommentListResponseDto(expectedComments);

            when(newsService.getCommentsByNewsId(newsId, PageRequest.of(pageNumber, pageSize)))
                    .thenReturn(expected);

            mockMvc.perform(get(NEWS_URL + newsId + COMMENTS)
                            .param(PAGE, String.valueOf(pageNumber))
                            .param(SIZE, String.valueOf(pageSize)))
                    .andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(expected)));

            verify(newsService, times(PAGE_NUMBER))
                    .getCommentsByNewsId(newsId, PageRequest.of(pageNumber, pageSize));
        }

        @Test
        void getCommentsByNewsIdShouldReturnNotFound_whenInvalidNewsId() throws Exception {
            Long invalidNewsId = INVALID_ID;

            when(newsService.getCommentsByNewsId(invalidNewsId, PageRequest.of(PAGE_NUMBER, PAGE_SIZE)))
                    .thenThrow(new EntityNotFoundExceptionCustom
                            (NEWS1 + PREFIX_NOT_FOUND_CUSTOM_MESSAGE + invalidNewsId
                                    + POSTFIX_NOT_FOUND_CUSTOM_MESSAGE));

            mockMvc.perform(get(NEWS_URL + invalidNewsId + COMMENTS)
                            .param(PAGE, STRING_ONE)
                            .param(SIZE, STRING_ONE_FIVE))
                    .andExpect(status().isNotFound());
        }
    }
}
