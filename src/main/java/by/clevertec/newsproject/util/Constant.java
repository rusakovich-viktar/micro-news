package by.clevertec.newsproject.util;

import lombok.experimental.UtilityClass;

/**
 * Утилитарный класс для хранения констант.
 */
@UtilityClass
public class Constant {

    /**
     * Константы для API новостей.
     */
    @UtilityClass
    public class BaseApi {

        public static final String COMMENTS_NEWS_NEWS_ID = "/comments/news/{newsId}";
        public static final String NEWS = "/news";
        public static final String ID = "/{id}";
        public static final String NEWS_ID_COMMENTS = "/{newsId}/comments";

    }

    /**
     * Константы для сообщений.
     */
    @UtilityClass
    public class Messages {

        public static final String TEXT_CANNOT_BE_BLANK = "Text cannot be blank";
        public static final String TITLE_CANNOT_BE_BLANK = "Title cannot be blank";
        public static final String NEWS_WITH_ID_WAS_RETRIEVED_FROM_CACHE = "News with id {} was retrieved from cache";
        public static final String NEWS_WITH_ID_WAS_NOT_FOUND_IN_CACHE_RETRIEVING_FROM_DATABASE = "News with id {} was not found in cache, retrieving from database";
        public static final String NEWS_WITH_ID_WAS_ADDED_TO_CACHE = "News with id {} was added to cache";
        public static final String NEWS_WITH_ID_WAS_REMOVED_FROM_CACHE = "News with id {} was removed from cache";
        public static final String NEWS_WITH_ID_WAS_UPDATED_IN_CACHE = "News with id {} was updated in cache";
        public static final String ERROR_RETRIEVING_COMMENTS_FOR_NEWS_ID = "Error retrieving comments for newsId: ";
    }

    /**
     * Константы для атрибутов.
     */
    @UtilityClass
    public class Atrubutes {

        public static final String NEWS = "news";
    }

}
