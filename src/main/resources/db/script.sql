INSERT INTO news (time, update_time, title, text)
VALUES (NOW(), NOW(), 'Новость 1', 'Текст новости 1')
RETURNING id;

-- Предположим, что возвращенный ID равен 1

INSERT INTO comments (time, update_time, text, username, news_id)
VALUES (NOW(), NOW(), 'Комментарий 1', 'Пользователь 1', 1),
       (NOW(), NOW(), 'Комментарий 2', 'Пользователь 2', 1);