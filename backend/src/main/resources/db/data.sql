DELETE FROM sys_role_permission;
DELETE FROM sys_user_role;
DELETE FROM sys_permission;
DELETE FROM sys_role;
DELETE FROM sys_user;

INSERT INTO sys_user (`id`, `username`, `password`)
    VALUES (1, 'ADMIN', 'admin');

INSERT INTO sys_role (`id`, `role_code`, role_name)
    VALUES (1,1,'administrator');

INSERT INTO sys_permission(`id`, `permission_name`, `permission_code`, `type`, `parent_id`)
    VALUES
        (1, 'DashBoard', 'dashboard', 'MENU', NULL),
        (2, 'User Management', 'user', 'MENU', NULL),
        (3, 'Role Management', 'role', 'MENU', NULL),
        (4, 'Permission Management', 'permission', 'MENU', NULL),
        (10, 'User List', 'user:list', 'MENU', 2),
        (11, 'Create User', 'user:create', 'BUTTON', 2),
        (12, 'Update User', 'user:update', 'BUTTON', 2),
        (13, 'Delete User', 'user:delete', 'BUTTON', 2),
        (20, 'Role List', 'role:list', 'MENU', 3),
        (21, 'Create Role', 'role:create', 'BUTTON', 3),
        (22, 'Update Role', 'role:update', 'BUTTON', 3),
        (23, 'Delete Role', 'role:delete', 'BUTTON', 3),
        (30, 'Permission List', 'permission:list', 'MENU', 4),
        (31, 'Create Permission', 'permission:create', 'BUTTON', 4),
        (32, 'Update Permission', 'permission:update', 'BUTTON', 4),
        (33, 'Delete Permission', 'permission:delete', 'BUTTON', 4);

INSERT INTO sys_user_role(`user_id`, `role_id`)
    VALUES (1, 1);

INSERT INTO sys_role_permission(`role_id`, `permission_id`)
    VALUES
        (1, 1),
        (1, 2),
        (1, 3),
        (1, 4),
        (1, 10),
        (1, 11),
        (1, 12),
        (1, 13),
        (1, 20),
        (1, 21),
        (1, 22),
        (1, 23),
        (1, 30),
        (1, 31),
        (1, 32),
        (1, 33)