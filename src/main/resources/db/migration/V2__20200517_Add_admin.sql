INSERT INTO usr (id, active, password, username)
VALUES (1, true, '123', 'admin');

INSERT INTO user_role (user_id, roles)
VALUES (1, 'USER')