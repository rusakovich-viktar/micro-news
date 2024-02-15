INSERT INTO news (time, update_time, title, text)
VALUES (NOW(), NOW(), 'Новость 1', 'Текст новости 1')
RETURNING id;

-- Предположим, что возвращенный ID равен 1

INSERT INTO comments (time, update_time, text, username, news_id)
VALUES (NOW(), NOW(), 'Комментарий 1', 'Пользователь 1', 1),
       (NOW(), NOW(), 'Комментарий 2', 'Пользователь 2', 1);


INSERT INTO news (time, update_time, title, text)
VALUES (NOW(), NOW(), 'Новость 22', 'Текст новости 2'),
       (NOW(), NOW(), 'Новинки', 'привет текст 2'),
       (NOW(), NOW(), 'текст в названии', 'вот это новость я не знаю'),
       (NOW(), NOW(), 'новокаша', 'текстовыделитель')

RETURNING id;

create table news
(
    id          bigserial
        primary key,
    time        timestamp(6),
    update_time timestamp(6),
    text        varchar(255) not null,
    title       varchar(255) not null
);


