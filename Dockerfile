FROM gradle:8.5-jdk-alpine AS build

WORKDIR /app1-news

COPY . /app1-news

RUN gradle build --no-daemon

FROM openjdk:17-alpine3.14

COPY --from=build /app1-news/build/libs/news-project-0.0.1-SNAPSHOT.jar /app.jar

CMD ["java", "-jar", "/app.jar"]
