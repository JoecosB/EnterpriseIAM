DROP TABLE IF EXISTS sys_user;
CREATE TABLE sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE,
    status TINYINT NOT NULL DEFAULT 1,
    deleted TINYINT NOT NULL DEFAULT 0,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

DROP TABLE IF EXISTS sys_role;
CREATE TABLE sys_role(
    id SMALLINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    role_code SMALLINT NOT NULL UNIQUE,
    role_name VARCHAR(50) NOT NULL,
    description VARCHAR(100)
);

DROP TABLE IF EXISTS sys_permission;
CREATE TABLE sys_permission (
    id SMALLINT PRIMARY KEY AUTO_INCREMENT,
    permission_name VARCHAR(50) NOT NULL,
    permission_code VARCHAR(100) NOT NULL UNIQUE,
    type TINYINT NOT NULL,
    parent_id SMALLINT DEFAULT NULL,
    path VARCHAR(200),
    component VARCHAR(200),
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

DROP TABLE IF EXISTS sys_user_role;
CREATE TABLE sys_user_role (
    user_id BIGINT NOT NULL,
    role_id SMALLINT NOT NULL,
    PRIMARY KEY (user_id, role_id)
    );

DROP TABLE IF EXISTS sys_role_permission;
CREATE TABLE sys_role_permission (
    role_id SMALLINT NOT NULL,
    permission_id SMALLINT NOT NULL,
    PRIMARY KEY (role_id, permission_id)
);