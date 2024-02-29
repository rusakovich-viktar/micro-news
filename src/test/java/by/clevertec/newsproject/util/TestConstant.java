package by.clevertec.newsproject.util;

import java.time.LocalDateTime;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TestConstant {

    public static final long ID_ONE = 1L;
    public static final LocalDateTime LOCAL_DATE_TIME = LocalDateTime.of(2024, 2, 25, 12, 12, 12);
    public static final String NEWS_TITLE = "Название новости";
    public static final String NEWS_TEXT = "Тут должно быть много слов по тексту новости";
    public static final String USERNAME = "Username";
    public static final String NEW_TITLE = "new title";
    public static final String NEW_TEXT = "new text";

    public static final String ID = "{id}";

    public static final long INVALID_ID = 999L;
    public static final String NEWS1 = "News";
    public static final String PAGE = "page";
    public static final String SIZE = "size";

    public static final String STRING_ONE = "1";
    public static final String STRING_ONE_FIVE = "15";
    public static final int PAGE_NUMBER = 1;
    public static final int PAGE_SIZE = 15;

    @UtilityClass
    public class ExceptionMessages {

        public static final String PREFIX_NOT_FOUND_CUSTOM_MESSAGE = " with id ";
        public static final String POSTFIX_NOT_FOUND_CUSTOM_MESSAGE = " does not exist";

        public static final String EXCEPTION_OCCURRED_DURING_TEST = "Exception occurred during test: ";

        public static final String ERROR_RETRIEVING_COMMENTS_FOR_NEWS_ID = "Error retrieving comments for newsId: ";
    }

    @UtilityClass
    public class Attributes {

        public static final String ONE = "one";
        public static final String TWO = "two";
        public static final String THREE = "three";
    }

    @UtilityClass
    public class Path {

        public static final String SIZE_5_PAGE_0 = "?size=5&page=0";
        public static final String COMMENTS_NEWS = "/comments/news/";
        public static final String NEWS_URL = "/news/";
        public static final String NEWS = "/news";
        public static final String HTTP_LOCALHOST = "http://localhost:";

        public static final String COMMENTS = "/comments";
    }


}

