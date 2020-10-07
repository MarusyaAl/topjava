DELETE
FROM user_roles;
DELETE
FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('User2', 'user2@yandex.ru', 'password2'),
       ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id)
VALUES ('ROLE_USER', 100000),
       ('ROLE_USER2', 100002),
       ('ROLE_ADMIN', 100001);

INSERT INTO meals (date_time, description, calories, user_id)
VALUES ('2020-09-08 11:21:33', 'Спагетти', '200', 100000),
       ('2020-09-08 12:22:33', 'Тольятелле', '600', 100000),
       ('2020-09-08 13:25:33', 'Тортик', '950', 100000),
       ('2020-09-08 14:29:33', 'Гречка', '250', 100000),
       ('2020-01-30 11:10:00', 'Банан', '235', 100002),
       ('2020-04-18 12:00:00', 'Макароны с котлетой', '420', 100002),
       ('2020-06-08 13:35:00', 'Сосиски с горошком', '380', 100002);


