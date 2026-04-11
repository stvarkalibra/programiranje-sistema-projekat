INSERT INTO roles (name) VALUES ('ADMIN');
INSERT INTO roles (name) VALUES ('GUIDE');
INSERT INTO roles (name) VALUES ('GUEST');

INSERT INTO app_users (username, api_key) VALUES ('adminUser', 'admin-key-123');
INSERT INTO app_users (username, api_key) VALUES ('guideUser', 'guide-key-456');
INSERT INTO app_users (username, api_key) VALUES ('guestUser', 'guest-key-789');

INSERT INTO user_role (user_id, role_id) VALUES (1, 1);
INSERT INTO user_role (user_id, role_id) VALUES (2, 2);
INSERT INTO user_role (user_id, role_id) VALUES (3, 3);