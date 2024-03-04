FROM openjdk:17-alpine3.14
COPY /build/libs/news-project-0.0.1.jar /micro-news.jar
CMD ["java", "-jar", "/micro-news.jar"]