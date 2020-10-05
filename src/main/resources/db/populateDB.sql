DELETE FROM user_roles;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password) VALUES
  ('User', 'user@yandex.ru', 'password'),
  ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 100000),
  ('ROLE_ADMIN', 100001);

INSERT INTO meals (datetime,description, calories, user_id) VALUES
('2020-09-08 11:21:33','Спагетти', '200',100000),
('2020-09-08 12:22:33','Тольятелле', '600',100000),
('2020-09-08 13:25:33','Тортик', '950',100000),
('2020-09-08 14:29:33','Гречка', '250',100000);